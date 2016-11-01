SELECT
  (CASE WHEN (DHVPOHP.DHCMPN = ' 31') THEN '153776'
        WHEN (DHVPOHP.DHCMPN = ' 18') THEN '155439'
   ELSE '' END)                                                AS Vendor_ID,
  (CASE WHEN (DHVPOHP.DHCMPN = ' 31') THEN '25'
        WHEN (DHVPOHP.DHCMPN = ' 18') THEN '08'
   ELSE '' END)                                                AS DC,
  Trim(FVVNDAP.FVVNDN)                                         AS Supplier_ID,
  (CASE
     WHEN (Left(XVVNDAP.XVUA32, 1) = 'Y') THEN 'T'
     ELSE 'P'
   END)                                                        AS Order_Type_Key,
  'S'                                                          AS Line_Type_Key,
  DHVPOHP.DHPONB                                               AS ORDER_NUMBER,
  DIVPOIP.DILNDI                                               AS ORDER_LINE_NUMBER,
  (CASE
     WHEN (DHDPSH = 'Y') THEN 'D'
     WHEN (DHBHLC = 'Y' AND Left(DHZHAT,1) = 'Y') THEN 'P'
     WHEN (DHBHLC = 'N' AND Left(DHZHAT,1) = 'Y') THEN 'P'
     WHEN (Left(DHZHAT,1) = ' ' OR Left(DHZHAT,1) = 'N') THEN 'D'
     WHEN (Left(DHZHAT,1) = 'B') THEN 'P'
     WHEN (Left(DHZHAT,1) = 'R') THEN 'D'
     ELSE 'U'
   END)                                                        AS SHIPMENT_RESPONSIBILITY,
  'NA'                                                         AS carrier_id,
  0                                                            AS pickup_date,
  HSDATA.F_CONVERT_YYYYMMDD_TO_DATE(20000000 + DHVPOHP.DHDTET) AS ORDER_DATE,
  HSDATA.F_CONVERT_YYYYMMDD_TO_DATE(20000000 + DHVPOHP.DHDTEQ) AS REQUESTED_ARRIVAL,
  DIVPOIP.DIQYOA                                               AS QUANTITY_ORDERED,
  0                                                            AS QUANTITY_RECEIVED,
  0                                                            AS LOT_NUMBER,
  (CASE FIITMAP.FICWCD
      WHEN 'Y' THEN DIVPOIP.DICSPX * FIITMAP.FIWTIW
      ELSE  DIVPOIP.DICSPX
   END)                                                        AS FOB_ORIGIN,
  (DIVPOIP.DICSPX +
    (CASE
       WHEN (DHVPOHP.DHFCSE = 0) THEN DIVPOIP.DIFRTX
       ELSE DHVPOHP.DHFCSE
     END)) *
  (CASE FIITMAP.FICWCD
      WHEN 'Y' THEN FIITMAP.FIWTIW
      ELSE 1
   END)                                                        AS delivered_price,
  (CASE
     WHEN (DHVPOHP.DHFCSE = 0) THEN DIVPOIP.DIFRTX
     ELSE DHVPOHP.DHFCSE
   END)  *
  (CASE FIITMAP.FICWCD
      WHEN 'Y' THEN FIITMAP.FIWTIW
      ELSE 1
   END)                                                        AS FREIGHT_COST,
  Trim(FIITMAP.FIIDE1)                                         AS item_description_raw,
  Trim(DHCMPN)                                                 AS COMPANY,
  Trim(DHDIVN)                                                 AS DIVISION,
  Trim(DHDPTN)                                                 AS DEPARTMENT,
  FIITMAP.FIITMN                                               AS Maines_Item_Number

FROM
  HSDATA.DHVPOHP DHVPOHP INNER JOIN HSDATA.DIVPOIP DIVPOIP ON
      DHVPOHP.DHCMPN = DIVPOIP.DICMPN and
      DHVPOHP.DHDIVN = DIVPOIP.DIDIVN and
      DHVPOHP.DHDPTN = DIVPOIP.DIDPTN and
      DHVPOHP.DHVNDN = DIVPOIP.DIVNDN and
      DHVPOHP.DHPONB = DIVPOIP.DIPONB

                         INNER JOIN HSDATA.FJITMBP FJITMBP ON
      DIVPOIP.DICMPN = FJITMBP.FJCMPN and
      DIVPOIP.DIDIVN = FJITMBP.FJDIVN and
      DIVPOIP.DIDPTN = FJITMBP.FJDPTN and
      DIVPOIP.DIWHSN = FJITMBP.FJWHSN and
      DIVPOIP.DIITMN = FJITMBP.FJITMN

                         INNER JOIN HSDATA.ZHITMXP ZHITMXP ON
      FJITMBP.FJCMPN = ZHITMXP.ZHCMPN and
      FJITMBP.FJDIVN = ZHITMXP.ZHDIVN and
      FJITMBP.FJDPTN = ZHITMXP.ZHDPTN and
      FJITMBP.FJWHSN = ZHITMXP.ZHWHSN and
      FJITMBP.FJITMN = ZHITMXP.ZHITMN

                         INNER JOIN HSDATA.FIITMAP FIITMAP ON
      FJITMBP.FJCMPN = FIITMAP.FICMPN and
      FJITMBP.FJITMN = FIITMAP.FIITMN

                         INNER JOIN HSDATA.XVVNDAP XVVNDAP ON
      DHVPOHP.DHCMPN = XVVNDAP.XVCMPN and
      DHVPOHP.DHVNDN = XVVNDAP.XVVNDN

                         Inner Join HSDATA.FVVNDAP FVVNDAP on
      DIVPOIP.DICMPN = FVVNDAP.FVCMPN AND
      DIVPOIP.DIVNDN = FVVNDAP.FVVNDN

WHERE
  DHVPOHP.DHCMPN IN (' 31', ' 18') AND DHVPOHP.DHDIVN = '  1' AND DHVPOHP.DHDPTN = '  1'
