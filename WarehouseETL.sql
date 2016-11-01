Select Results.CP, 
       Results.WH, 
       Results.RT, 
       Max(Results.DRUDT) as DRUDT, 
       Results.LPEM, 
       Results.UOW,
       Max(Results.SZ) as SZ, 
       Max(Results.EDT) as EDT,
       Results.EMPNB, 
       Results.LPSDT, 
       Results.LPFDT, 
       Results.EQUIPMENTTYPE, 
       Results.CONCEPT, 
       Results.VOICE

FROM ( SELECT  WSHPDHH.DRCP        AS CP,        /*COMPANY*/
               WSHPDHH.DRWH        AS WH,        /*WAREHOUSE*/
               WSHPDHH.DRRT        AS RT,        /*ROUTE, ROUTEVAL*/
               WSHPDHH.DRUDT,
               WSHPBHH.BTUOW       AS UOW,       /*WORK ASSIGNMENT*/
               MAX(WSHPBHH.BTSZ)   AS SZ,        /*SELECTION ZONE, AREA*/
               MAX(WSHPBHH.BTEDT)  AS EDT,       /*CREATION DATE & TIME*/
               WEMPEMM.EMPNB,                    /*EMP_PayrollID*/
               MAX(WEMPLPT2.LPSDT) as LPSDT,     /*START DATE & TIME*/
               MAX(WEMPLPT2.LPFDT) as LPFDT,     /*FINISH, STOP DATE & TIME*/
               WEMPLPT2.LPEM,
               Max(WEMPLPT2.LPQTY) as TUnitC,    /*Emp LP Quantity*/
               'CP'                AS EQUIPMENTTYPE,
               ' '                 AS CONCEPT,       
               'N'                 AS VOICE

	FROM  IPWBSPRDF.WSHPDHH WSHPDHH	INNER JOIN IPWBSPRDF.WSHPBHH WSHPBHH ON
	               WSHPDHH.DRSDT = WSHPBHH.BTDAT
                   AND WSHPDHH.DRRT  = WSHPBHH.BTRT
                   AND WSHPDHH.DRWH  = WSHPBHH.BTWH 
                   AND WSHPDHH.DRCP  = WSHPBHH.BTCP

                                        INNER JOIN IPWBSPRDF.WEMPLPT2 WEMPLPT2 ON
                       WSHPDHH.DRCP  = WEMPLPT2.LPCP
                   AND WSHPDHH.DRWH  = WEMPLPT2.LPWH
                   AND WSHPBHH.BTUOW = WEMPLPT2.LPRN4

                                        LEFT JOIN IPWBSPRDF.WEMPEMM WEMPEMM ON
                       WSHPDHH.DRCP  = WEMPEMM.EMCP
                   AND WSHPDHH.DRWH  = WEMPEMM.EMWH
                   AND WEMPLPT2.LPEM = WEMPEMM.EMEM

	WHERE WEMPLPT2.LPLC = 'SP ' 
          AND WEMPLPT2.LPQTY > 0
          AND WSHPDHH.DRUDT between {$BeginTimeStamp} and {$EndTimeStamp}
--        AND WSHPDHH.DRUDT between 20100304070000 and 20100305065959
          AND WSHPBHH.BTUOW != 0

        GROUP BY WSHPDHH.DRCP, 
                 WSHPDHH.DRWH,
                 WSHPBHH.BTUOW,       
                 WSHPDHH.DRRT,
                 WEMPEMM.EMPNB,
                 WEMPLPT2.LPEM,
                 WSHPDHH.DRUDT,
                 WSHPBHH.BTSPA
       ) as Results

Group By Results.CP, Results.WH, Results.RT, Results.LPEM,
         Results.UOW, Results.EMPNB, Results.LPSDT,
         Results.LPFDT, Results.EQUIPMENTTYPE,
         Results.CONCEPT, Results.VOICE
