SELECT DISTINCT
      'DV4'                                                       AS RECORD_CODE,
      RSI_Product_Master.DC_NO                                    AS DISTRIBUTOR_CODE,
      MAINES_ITEM_NUMBER                                          AS ITEM_NO,
      QTY_ON_HAND - COALESCE(CASES_RECEIVED_TODAY,0)              AS QUANTITY_ON_HAND,
      COALESCE(QUAN_RSRV_FOR_BK, '0')                             AS QUAN_RSRVD_FOR_BK,
      QTY_ON_HAND - COALESCE(CASES_RECEIVED_TODAY,0)
                  - COALESCE(QUAN_RSRV_FOR_BK, '0')               AS QUAN_AVAILABLE,
      COALESCE(QTY_ON_PO, '0') + COALESCE(CASES_RECEIVED_TODAY,0) AS QUAN_ORDERED,
      COALESCE(ROUND((
                      CASE AVG_SALES_PER_DAY WHEN '0' THEN '0'
                      ELSE (QTY_ON_HAND - COALESCE(CASES_RECEIVED_TODAY,0))/AVG_SALES_PER_DAY
                      END)
                     , 0), '0')                                   AS DAYS_SUPPLY,      
      GTIN                                                        AS GLOBAL_TRADE_ITEM_NO

FROM RSI_Product_Master LEFT OUTER JOIN RSI_Inventory_ORD ON
           RSI_Product_Master.DC_NO              = RSI_Inventory_ORD.DC_NO     AND
           RSI_Product_Master.MAINES_ITEM_NUMBER = RSI_Inventory_ORD.DC_PROD_CODE

                         LEFT OUTER JOIN 
     (SELECT DIST_CNTR_NO,
             DIST_PROD_CODE,
             Sum(QUAN_SHIPPED) / Count(INVOICE_DATE_YYMMDD) AS AVG_SALES_PER_DAY
      FROM RSI_ItemShipped_ByDay
      GROUP BY DIST_CNTR_NO, DIST_PROD_CODE) AS ShippedByDays on
           RSI_Product_Master.DC_NO              = ShippedByDays.DIST_CNTR_NO AND
           RSI_Product_Master.MAINES_ITEM_NUMBER = ShippedByDays.DIST_PROD_CODE

                          LEFT OUTER JOIN 
     (SELECT SHIP_TO_DC_ID,
             DISTRIBUTOR_PRODUCT_CODE,
             SUM(CASES_ORDERED)       AS CASES_RECEIVED_TODAY
      FROM RSI_Open_PO
      WHERE TYPE = 'Closed'
      GROUP BY SHIP_TO_DC_ID, DISTRIBUTOR_PRODUCT_CODE) AS Todays_Receipts on
           RSI_Product_Master.DC_NO              = Todays_Receipts.SHIP_TO_DC_ID AND
           RSI_Product_Master.MAINES_ITEM_NUMBER = Todays_Receipts.DISTRIBUTOR_PRODUCT_CODE

GROUP BY RSI_Product_Master.DC_NO, MAINES_ITEM_NUMBER, QTY_ON_HAND,
         QUAN_RSRV_FOR_BK, QTY_ON_PO, GTIN, AVG_SALES_PER_DAY,
         CASES_RECEIVED_TODAY

ORDER BY MAINES_ITEM_NUMBER, RSI_Product_Master.DC_NO desc
