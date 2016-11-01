WITH
DATE_SENSITIVE_ITEMS (PW_Company, PW_Warehouse, Item, Shelf_Date, Expiraton_Type, Cases) as
    (SELECT WWHSPAT.PACP,
            WWHSPAT.PAWH,
            WWHSPAT.PAIT,
            (CASE WITMITM.ITEXF
                WHEN 'P' THEN WWHSPAT.PASLF
                WHEN 'E' THEN WWHSPAT.PASLF
                ELSE 0
             END) AS Shelf_Date,
            WITMITM.ITEXF,
            SUM(WWHSPAT.PAQTY / WUTMUMM.UMRAT)
     FROM IPWBSPRDF.WWHSPAT WWHSPAT INNER JOIN IPWBSPRDF.WUTMUMM WUTMUMM ON
            WWHSPAT.PACP  = WUTMUMM.UMCP AND
	     WWHSPAT.PAIUM = WUTMUMM.UMUM
                                    INNER JOIN IPWBSPRDF.WITMITM WITMITM ON
            WWHSPAT.PACP  = WITMITM.ITCP AND
            WWHSPAT.PAIT  = WITMITM.ITIT
     WHERE WWHSPAT.PACP IN (' 31', ' 18') AND
           WWHSPAT.PAWH = '  1' AND
           WWHSPAT.PASTS <> 'H'
     GROUP BY WWHSPAT.PACP,
             WWHSPAT.PAWH,
             WWHSPAT.PAIT,
             (CASE WITMITM.ITEXF
                WHEN 'P' THEN WWHSPAT.PASLF
                WHEN 'E' THEN WWHSPAT.PASLF
                ELSE 0
              END),
             WITMITM.ITEXF  ),

FIRST_LOT (Item, Shelf_Date) as
    (SELECT DATE_SENSITIVE_ITEMS.Item,
            Min(DATE_SENSITIVE_ITEMS.Shelf_Date) as First_Shelf_Date
     FROM DATE_SENSITIVE_ITEMS
     GROUP BY DATE_SENSITIVE_ITEMS.Item)

SELECT
   (CASE WHEN (FJITMBP.FJCMPN = ' 31') THEN '153776'
         WHEN (FJITMBP.FJCMPN = ' 18') THEN '155439'
   ELSE '' END)                                             AS Vendor_ID,
  (CASE WHEN (FJITMBP.FJCMPN = ' 31') THEN '25'
        WHEN (FJITMBP.FJCMPN = ' 18') THEN '08'
   ELSE '' END)                                             AS location_id,
   DATE_SENSITIVE_ITEMS.Shelf_Date                          AS LotNum_raw,
   DATE_SENSITIVE_ITEMS.Cases                               AS quantity_on_hand,
   (CASE
      WHEN DATE_SENSITIVE_ITEMS.Shelf_Date = FIRST_LOT.Shelf_Date THEN FJITMBP.FJQCAL
      ELSE 0
    END)                                                    AS quantity_committed,
   (CASE FIITMAP.FICWCD
       WHEN 'Y' THEN FJITMBP.FJCSTB * FIITMAP.FIWTIW
       ELSE FJITMBP.FJCSTB
    END)                                                    AS price,
   FIITMAP.FIIDE1                                           AS description_raw,
   Trim(FJITMBP.FJCMPN)                                     AS company,
   Trim(FJITMBP.FJDIVN)                                     AS division,
   Trim(FJITMBP.FJDPTN)                                     AS department,
   FJITMBP.FJITMN                                           AS maines_item

FROM HSDATA.FJITMBP FJITMBP inner join HSDATA.FIITMAP FIITMAP on
         FJITMBP.FJCMPN = FIITMAP.FICMPN and
         FJITMBP.FJITMN = FIITMAP.FIITMN

                            left outer join DATE_SENSITIVE_ITEMS on
         FIITMAP.FICMPN = DATE_SENSITIVE_ITEMS.PW_Company and
         '  1'          = DATE_SENSITIVE_ITEMS.PW_Warehouse and
         FIITMAP.FIITMN = DATE_SENSITIVE_ITEMS.Item

                            left outer join FIRST_LOT on
         DATE_SENSITIVE_ITEMS.Item       = FIRST_LOT.Item AND
         DATE_SENSITIVE_ITEMS.Shelf_Date = FIRST_LOT.Shelf_Date

WHERE FJITMBP.FJCMPN IN (' 31', ' 18') AND FJITMBP.FJDIVN = '  1' AND
      FJITMBP.FJDPTN = '  1' AND FJITMBP.FJWHSN = '  1' AND
     ((FJITMBP.FJARCD = 'A') or (FJITMBP.FJARCD = 'D' and FJITMBP.FJQOHH > 0))
