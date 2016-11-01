/*******************************************************************************************************
 * SQL_Source.java
 *
 * Last modified: 2007-03-29
 *
 *  Source for all SQL statements used by the Routing Interface application.  See below for database-
 *  table information
 *************************************************************************************************/
package maines.transportation.routing.RoutingInterface;

/**
 *
 * @author  Bill Kimler
 */
public class SQL_Source {
   /********************************************************************************************************************
    * SQL statements used by OrderDownload   
    ********************************************************************************************************************/
   public static final String OD_Override_OHORDHL0_ORDER = 
      "call qsys.qcmdexc('OVRDBF FILE(OHORDHL0) TOFILE(HSDATA/OHORDHL0) MBR(ORDER) OVRSCOPE(*JOB)',0000000071.00000)";

   public static final String OD_Override_OHORDHL0_DAYEND = 
      "call qsys.qcmdexc('OVRDBF FILE(OHORDHL0) TOFILE(HSDATA/OHORDHL0) MBR(DAYEND) OVRSCOPE(*JOB)',0000000072.00000)";

   public static final String OD_Override_OHORDHL0_REPORT = 
      "call qsys.qcmdexc('OVRDBF FILE(OHORDHL0) TOFILE(HSDATA/OHORDHL0) MBR(REPORT) OVRSCOPE(*JOB)',0000000072.00000)";

   public static final String OD_Override_OIORDDL0_ORDER = 
      "call qsys.qcmdexc('OVRDBF FILE(OIORDDL0) TOFILE(HSDATA/OIORDDL0) MBR(ORDER) OVRSCOPE(*JOB)',0000000071.00000)";

   public static final String OD_Override_OIORDDL0_DAYEND = 
       "call qsys.qcmdexc('OVRDBF FILE(OIORDDL0) TOFILE(HSDATA/OIORDDL0) MBR(DAYEND) OVRSCOPE(*JOB)',0000000072.00000)";

   public static final String OD_Override_OIORDDL0_REPORT = 
       "call qsys.qcmdexc('OVRDBF FILE(OIORDDL0) TOFILE(HSDATA/OIORDDL0) MBR(REPORT) OVRSCOPE(*JOB)',0000000072.00000)";
   
   
   public static final String OD_OrderCount = 
      "SELECT Count(OHORNR) AS OrderCount " +
      "FROM HSDATA.OHORDHL0 " +
      "WHERE OHCMPN = ? AND OHDIVN = ? AND OHDPTN = ? AND OHARCD <>'D' AND OHCRIN <> 'Y'"; 

   public static final String OD_OrderData = 
      "SELECT OHRTEN AS TripNumber, OHDTES AS ShipDate, OHSTPN AS StopNumber, OHORNR AS OrderNumber, OHPKPR AS PickticketPrintedFlag, " +
      "       Sum(OIQYSA) AS Pieces, Sum(OIEXCB) AS Cube, Sum(OIEXSH) AS Weight, OHCUSN AS BillToCustomer, OHZHAT AS ShipToCustomer, " +
      "       OPZHA3 AS WarehouseArea " +
      "FROM HSDATA.OHORDHL0, HSDATA.OIORDDL0, HSDATA.OPWHAAL0 " +
      "WHERE OHCMPN = ? AND OHDIVN = ? AND OHDPTN = ? AND OHWHSN = ? AND OHARCD <> 'D' AND OHCRIN <> 'Y' AND OIARCD <> 'D' AND " +
      "      Left(OHRTEN,3) <> '  X' AND " +
      "      OHCMPN = OICMPN AND OHDIVN = OIDIVN AND OHDPTN = OIDPTN AND OHWHSN = OIWHSN AND OHORNR = OIORNR AND " +
      "      OICMPN = OPCMPN AND OIDIVN = OPDIVN AND OIDPTN = OPDPTN AND OIWHSN = OPWHSN AND OIWHNA = OPWHNA " +
      "GROUP BY OHRTEN, OHDTES, OHSTPN, OHORNR, OHPKPR, OHCUSN, OHZHAT, OPZHA3";

   public static final String OD_OrderData_Darden = 
       "SELECT OHRTEN AS TripNumber, OHDTES AS ShipDate, OHSTPN AS StopNumber, OHORNR AS OrderNumber, OHPKPR AS PickticketPrintedFlag, " +
       "       OIQYSA AS Pieces, OIEXCB AS Cube, OIEXSH AS Weight, OIITMN as ItemNumber, OHCUSN AS BillToCustomer, OHZHAT AS ShipToCustomer, " +
       "       OPZHA3 AS WarehouseArea " +
       "FROM HSDATA.OHORDHL0, HSDATA.OIORDDL0, HSDATA.OPWHAAL0 " +
       "WHERE OHCMPN = ? AND OHDIVN = ? AND OHDPTN = ? AND OHWHSN = ? AND OHARCD <> 'D' AND OHCRIN <> 'Y' AND OIARCD <> 'D' AND " +
       "      Left(OHRTEN,3) <> '  X' AND " +
       "      OHCMPN = OICMPN AND OHDIVN = OIDIVN AND OHDPTN = OIDPTN AND OHWHSN = OIWHSN AND OHORNR = OIORNR AND " +
       "      OICMPN = OPCMPN AND OIDIVN = OPDIVN AND OIDPTN = OPDPTN AND OIWHSN = OPWHSN AND OIWHNA = OPWHNA " +
       "ORDER BY OHORNR, OPZHA3";
   
   public static final String OD_TripList =
      "SELECT ERRTEN as TripNumber, ERRMME as TripName " + 
      "FROM HSDATA.ERRTMAL0 " +
      "WHERE ERCMPN = ? AND ERDIVN = ? AND ERDPTN = ?";

   public static final String OD_CustomerList =
      "SELECT FDCUSN as CustomerNumber, FDCNMB as CustomerName " + 
      "FROM HSDATA.FDCSTBL0 " +
      "WHERE FDCMPN = ? AND FDDIVN = ? AND FDDPTN = ?";
      
   public static final String OD_OrdersSentToRouting = 
      "SELECT ORDER_NUMBER as OrderNumber " +
      "FROM TSDBA.RS_ORDER " +
      "WHERE LOC_REG_ID_PREF_ORIGIN = ? AND DATE_ADDED > ? " +
      "GROUP BY ORDER_NUMBER";
   
   public static final String OD_SetOrderInUseFlag = 
       "UPDATE HSDATA.OHORDHO5 " +
       "SET OHIUSE = 'Y', OHUSR2 = 'ROUTING' " +
       "WHERE OHCMPN = ? AND OHDIVN = ? AND OHDPTN = ? AND OHCUSN = ? AND OHORNR = ? AND OHIUSE <> 'Y'";
      
   public static final String OD_SkeletonRoutes_Details = 
       "SELECT TSDBA.RS_STANDARD_STOP.ROUTE_ID as RouteID, TSDBA.RS_STANDARD_STOP.LOCATION_ID as LocationID, TSDBA.TS_LOCATION.DESCRIPTION as Description " +
       "FROM TSDBA.RS_STANDARD_STOP INNER JOIN TSDBA.TS_LOCATION " +
       "     ON TSDBA.RS_STANDARD_STOP.REGION_ID = TSDBA.TS_LOCATION.REGION_ID AND TSDBA.RS_STANDARD_STOP.LOCATION_ID = TSDBA.TS_LOCATION.ID " +
       "WHERE TSDBA.RS_STANDARD_STOP.REGION_ID = ? AND TSDBA.RS_STANDARD_STOP.LOCATION_TYPE = 'SIT' " +
       "ORDER BY TSDBA.RS_STANDARD_STOP.ROUTE_ID";
   
   /********************************************************************************************************************
    * SQL statements used by Location Synchronizer   
    ********************************************************************************************************************/
   public static final String LS_AddedHostAccounts = 
      "SELECT FCCUSN AS CustomerNumber " +
      "FROM HSDATA.FCCSTAL0 JOIN HSDATA.FDCSTBL0 " +
      "ON (FCCUSN = FDCUSN) AND (FCCMPN = FDCMPN) " +
      "WHERE FDCMPN = ? AND FDDIVN = ? AND FDDPTN = ? AND FDWHSN = ? AND FDHCDE = 'Y' AND FCARCD <> 'D' " +
      "  AND FDDTBC Between ? And ? AND SUBSTR(FDCUSN, 8, 3) <> '999'";

   public static final String LS_AddedHostAccounts_All = 
       "SELECT FCCUSN AS CustomerNumber " +
       "FROM HSDATA.FCCSTAL0 JOIN HSDATA.FDCSTBL0 " +
       "ON (FCCUSN = FDCUSN) AND (FCCMPN = FDCMPN) " +
       "WHERE FDCMPN = ? AND FDDIVN = ? AND FDDPTN = ? AND FDWHSN = ? AND FDHCDE = 'Y' AND FCARCD <> 'D' " +
       "  AND SUBSTR(FDCUSN, 8, 3) <> '999'";

   public static final String LS_DeletedHostAccounts = 
      "SELECT FCCUSN AS CustomerNumber " +
      "FROM HSDATA.FCCSTAL0 JOIN HSDATA.FDCSTBL0 " +
      "ON (FCCUSN = FDCUSN) AND (FCCMPN = FDCMPN) " +
      "WHERE FDCMPN = ? AND FDDIVN = ? AND FDDPTN = ? AND FDWHSN = ? AND FDHCDE = 'Y' AND FCARCD = 'D' " +
      "  AND SUBSTR(FDCUSN, 8, 3) <> '999'";
      
   public static final String LS_LookupSuspendedStatus = 
      "SELECT CRFLG2 as StopShipFlag " +
      "FROM NMCFL58.ARCUSTMR " +
      "WHERE CRCUST = ? AND CRBRCH = ?";

   public static final String LS_RoutingLocations =
      "SELECT ID as CustomerNumber " +
      "FROM TSDBA.TS_LOCATION " +
      "WHERE REGION_ID = ?";
      
   public static final String LS_LocationDetailLookup = 
      "SELECT FCCUSN AS CustomerNumber, FDCNMB AS ShipToName, FDCA1B AS ShipToAddress1, FDCA2B AS ShipToAddress2, " +
      "       FDCTYB AS ShipToCity, FDSTEB AS ShipToState, FDZPCB AS ShipToZip, FDPHNB AS ShipToPhone, " +
      "       FDSLNB AS SalesRepNumber, FDINSI AS SpecialInstructions1, XWINS1 AS SpecialInstructions2, " +
      "       XWINS2 AS SpecialInstructions3 " +
      "FROM HSDATA.FCCSTAL0, HSDATA.FDCSTBL0, HSDATA.XWCSTBL0 " +
      "WHERE FDCMPN = ? AND FDDIVN = ? AND FDDPTN = ? AND FDWHSN = ? AND FDCUSN = ? AND FCCMPN = FDCMPN " +
      "  AND FCCUSN = FDCUSN AND FDCMPN = XWCMPN AND FDDIVN = XWDIVN AND FDDPTN = XWDPTN AND FDCUSN = XWCUSN";

   /********************************************************************************************************************
    * SQL statements used by OrderSchedule   
    ********************************************************************************************************************/
   public static final String OS_SkeletonRoutes = 
      "SELECT ROUTE_ID as RouteID, DESCRIPTION as Description, STANDARD_DAYS as StandardDays " +
      "FROM TSDBA.RS_STANDARD_ROUTE " + 
      "WHERE REGION_ID = ?";
      
   public static final String OS_CallSchedule = 
      "SELECT ROUTE, CUTDAY, CUTTIM " +
      "FROM OPERATIONS.CALLSCHD " +
      "WHERE CMPN = ? AND DIVN = ? AND WHSN = ?";
      
   public static final String OS_ClearCallSchedule = 
      "DELETE FROM OPERATIONS.CALLSCHD " +
      "WHERE CMPN = ? AND DIVN = ? AND WHSN = ?";

   public static final String OS_UpdateCallSchedule = 
      "INSERT INTO OPERATIONS.CALLSCHD (CMPN, DIVN, WHSN, ROUTE, CUTDAY, CUTTIM) " +
      "VALUES (?, ?, ?, ?, ?, ?)";

   /********************************************************************************************************************
    * SQL statements used by RouteUpload   
    ********************************************************************************************************************/
   public static final String RU_CurrentOrderData = 
      "SELECT OHORNR AS OrderNumber, OHPKPR as WHSelectedFlag, OHRTEN as RouteNumber, OHSTPN as StopNumber " +
      "FROM HSDATA.OHORDHL0 " +
      "WHERE OHCMPN = ? AND OHDIVN = ? AND OHDPTN = ? AND OHWHSN = ? AND OHARCD <> 'D' AND OHCRIN <> 'Y'";

   
   public static final String RU_UpdateHostOrderHeader = 
     "UPDATE HSDATA.OHORDHL0 SET OHRTEN = ?, OHSTPN = ?, OHIUSE = ' ', OHUSR2 = ' '" +
     "WHERE OHCMPN = ? AND OHDIVN = ? AND OHDPTN = ? AND OHORNR = ?";                   

   public static final String RU_UpdateHostOrderDetail = 
     "UPDATE HSDATA.OIORDDL0 SET OIRTEN = ?, OISTPN = ? " +
     "WHERE OICMPN = ? AND OIDIVN = ? AND OIDPTN = ? AND OIORNR = ?";           
   
   public static final String RU_InsertRoute =
       "INSERT INTO HSDATA.ERRTMAL0 (ERRMME, ERZHN9, ERZUN9, ERCMPN, ERDIVN, ERDPTN, ERRTEN ) " +
       "VALUES(?, ?, ?, ?, ?, ?, ?)";
      
   public static final String RU_UpdateRoute =
       "UPDATE HSDATA.ERRTMAL0 SET ERRMME = ?, ERZUN9 = ? " +
       "WHERE ERCMPN = ? AND ERDIVN = ? AND ERDPTN = ? AND ERRTEN = ?";
   
   

   /********************************************************************************************************************
    * SQL statements used by RU_RouteMasterUpload   
    ********************************************************************************************************************/
   public static final String RM_HostCustomerData = 
      "SELECT FDCUSN as CustomerNumber, FDCNMB as CustomerName, SUBSTR(FDZUAT,4,7) as DaysData, ETCUSN as RouteCustomer, ETRTF1 as MonRoute, " +
      "       ETRTS1 As MonStop, ETRTF2 as TueRoute, ETRTS2 As TueStop, ETRTF3 as WedRoute, ETRTS3 As WedStop, ETRTF4 As ThuRoute, " +
      "       ETRTS4 as ThuStop, ETRTF5 As FriRoute, ETRTS5 As FriStop, ETRTF6 as SatRoute, ETRTS6 As SatStop, " +
      "       ETRTF7 as SunRoute, ETRTS7 as SunStop " +
      "FROM HSDATA.FDCSTBL0 LEFT JOIN HSDATA.ETRTEAL0 ON (FDCUSN = ETCUSN) AND " +
      "                                                  (FDDPTN = ETDPTN) AND " +
      "                                                  (FDDIVN = ETDIVN) AND " +
      "                                                  (FDCMPN = ETCMPN) " +
      "WHERE FDCMPN = ? AND FDDIVN = ? AND FDDPTN = ?";
      
   public static final String RM_HostRoutes =
         "SELECT  ERRTEN as RouteNumber, ERRMME as RouteName, ERZUN9 as WHDepart " +
         "FROM HSDATA.ERRTMAL0 " +
         "WHERE ERCMPN = ? AND ERDIVN = ? AND ERDPTN = ?";

   public static final String RM_RouteSets = 
      "SELECT SET_ID as SetID, DESCRIPTION as Description " +
      "FROM TSDBA.RS_STANDARD_ROUTE_SET " +
      "WHERE REGION_ID = ?";
      
   public static final String RM_RouteSet_Routes = 
      "SELECT TSDBA.RS_STANDARD_ROUTE.ROUTE_ID as RouteID, TSDBA.RS_STANDARD_ROUTE.DESCRIPTION as Description, " +
      "       START_TIME as StartTime, LOAD_PRIORITY as WHDepart, " +
      "       STANDARD_DAYS as RouteDays, DAYS as RouteSetDays " +
      "FROM (TSDBA.RS_STANDARD_ROUTE_SET INNER JOIN TSDBA.RS_STANDARD_ROUTE_SET_DETAIL ON " +
      "     (TSDBA.RS_STANDARD_ROUTE_SET.SET_ID = TSDBA.RS_STANDARD_ROUTE_SET_DETAIL.SET_ID) AND " +
      "     (TSDBA.RS_STANDARD_ROUTE_SET.REGION_ID = TSDBA.RS_STANDARD_ROUTE_SET_DETAIL.REGION_ID)) " +
      "  INNER JOIN TSDBA.RS_STANDARD_ROUTE ON (TSDBA.RS_STANDARD_ROUTE_SET_DETAIL.ROUTE_ID = TSDBA.RS_STANDARD_ROUTE.ROUTE_ID) AND " +
      "     (TSDBA.RS_STANDARD_ROUTE_SET_DETAIL.REGION_ID = TSDBA.RS_STANDARD_ROUTE.REGION_ID) " +
      "WHERE TSDBA.RS_STANDARD_ROUTE_SET.REGION_ID = ? AND TSDBA.RS_STANDARD_ROUTE_SET.SET_ID = ?";
      
   public static final String RM_RouteSet_Locations = 
      "SELECT SEQUENCE_NUMBER as SequenceNumber, LOCATION_ID as LocationID, LOCATION_TYPE as LocationType " +
      "FROM TSDBA.RS_STANDARD_ROUTE INNER JOIN TSDBA.RS_STANDARD_STOP " +
      "  ON TSDBA.RS_STANDARD_ROUTE.REGION_ID = TSDBA.RS_STANDARD_STOP.REGION_ID AND " +
      "     TSDBA.RS_STANDARD_ROUTE.ROUTE_ID = TSDBA.RS_STANDARD_STOP.ROUTE_ID " +
      "WHERE TSDBA.RS_STANDARD_ROUTE.REGION_ID = ? AND TSDBA.RS_STANDARD_ROUTE.ROUTE_ID = ? AND SEQUENCE_NUMBER >= 0";
      
   public static final String RM_UpdateCustomerRoute = 
     "UPDATE HSDATA.ETRTEAL0 SET ETRTF1 = ?, ETRTS1 = ?, ETRTF2 = ?, ETRTS2 = ?, ETRTF3 = ?, ETRTS3 = ?, " +
                                "ETRTF4 = ?, ETRTS4 = ?, ETRTF5 = ?, ETRTS5 = ?, ETRTF6 = ?, ETRTS6 = ?, " +
                                "ETRTF7 = ?, ETRTS7 = ? " +
     "WHERE ETCMPN = ? AND ETDIVN = ? AND ETDPTN = ? AND ETCUSN = ?";

   public static final String RM_InsertCustomerRoute = 
     "INSERT INTO HSDATA.ETRTEAL0 (ETRTF1, ETRTS1, ETRTF2, ETRTS2, ETRTF3, ETRTS3, " +
                                  "ETRTF4, ETRTS4, ETRTF5, ETRTS5, ETRTF6, ETRTS6, " +
                                  "ETRTF7, ETRTS7, ETCMPN, ETDIVN, ETDPTN, ETCUSN) " +
     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    
   public static final String RM_UpdateCustomerDeliveryDays = 
     "UPDATE HSDATA.FDCSTBL0 SET FDZUAT = (SUBSTR(FDZUAT,1,3) CONCAT CAST(? AS CHAR(7))) " +
     "WHERE FDCMPN = ? AND FDDIVN = ? AND FDDPTN = ? AND FDCUSN = ?";                   
      
   public static final String RM_InsertRoute =
     "INSERT INTO HSDATA.ERRTMAL0 (ERRMME, ERZHN9, ERZUN9, ERCMPN, ERDIVN, ERDPTN, ERRTEN ) " +
     "VALUES(?, ?, ?, ?, ?, ?, ?)";
    
   public static final String RM_UpdateRoute =
     "UPDATE HSDATA.ERRTMAL0 SET ERRMME = ?, ERZHN9 = ?, ERZUN9 = ? " +
     "WHERE ERCMPN = ? AND ERDIVN = ? AND ERDPTN = ? AND ERRTEN = ?";

   /********************************************************************************************************************
    * SQL statements used by Roadnet classes   
    ********************************************************************************************************************/
   public static final String RN_SubstituteLocations = 
      "SELECT LOCATION_ID as LocationID, NEW_LOCATION_ID as NewLocationID " +
      "FROM TSDBA.TS_LOCATION_SUBSTITUTION " +
      "WHERE REGION_ID = ?"; 

   public static final String RN_Locations = 
      "SELECT ID, TYPE as Type, DESCRIPTION as Description, ADDR_LINE1 as Address1, ADDR_LINE2 as Address2, " +
      "       REGION1 AS CITY, REGION3 AS STATE, POSTAL_CODE AS ZIP, DELIVERY_DAYS as DeliveryDays, " +
      "       STANDARD_INSTRUCTIONS as StandardInstructions, ACCOUNT_TYPE_ID as AccountTypeID, " +
      "       USER_FIELD1 AS KEYDROP " +
      "FROM TSDBA.TS_LOCATION " +
      "WHERE REGION_ID = ? " +
      "ORDER BY ID";
      
   /********************************************************************************************************************
    * SQL statements used by Operations classes   
    ********************************************************************************************************************/
   public static final String OP_BackhaulAppointments = 
      "SELECT * FROM OPERATIONS.LBHPOSCHD3 " +
      "WHERE LBRDCS = ? AND (LBRDSP BETWEEN ? AND ?) " +
      "ORDER BY LBRDSP, LBRRTE";   
      
   public static final String OP_BackhaulAppointments_UpdateRouting =
      "UPDATE OPERATIONS.LBHPOSCHD4 SET LBRTEN = ?, LBDSPD = ?, LBDLM = ?, LBTLM = ?, LBUSRM = ? " +
      "WHERE LBRDCS = ? AND LBPON  = ?";

   public static final String OP_RouteHistory_Header_Insert =
      "INSERT INTO OPERATIONS.LRTHSTH (RHCMPN, RHDIVN, RHLOCN, RHRTEN, RHDSPD, RHPDPD, RHPDPT," +
                                      "RHPRTD, RHPRTT, RHPMIL, RHRDCS, RHDEPO, RHUSRC, RHDCR, RHTCR) " +
      "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

   public static final String OP_RouteHistory_Header_Delete =
      "DELETE FROM OPERATIONS.LRTHSTH " +
      "WHERE RHCMPN = ? AND RHDIVN = ? AND RHLOCN = ? AND RHRTEN = ? AND RHDSPD = ? ";

  
   public static final String OP_RouteHistory_Detail_Insert =
      "INSERT INTO OPERATIONS.LRTHSTD (RDCMPN, RDDIVN, RDLOCN, RDRTEN, RDDSPD, RDSTOP, RDLCID, RDATYP," +
                                      "RDPCS, RDWGT, RDCUB, RDPADT, RDPATM, RDPDDT, RDPDTM, RDKEYD," +
                                      "RDPDST, RDWND1, RDWND2, RDWND3, RDOTRS, RDUSRC, RDDCR, RDTCR, RDCUSN) " +
      "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

   public static final String OP_RouteHistory_Detail_Delete =
      "DELETE FROM OPERATIONS.LRTHSTD " +
      "WHERE RDCMPN = ? AND RDDIVN = ? AND RDLOCN = ? AND RDRTEN = ? AND RDDSPD = ? AND RDSTOP = ?";

   public static final String OP_RouteHistory_Order_Insert =
      "INSERT INTO OPERATIONS.LRTHSTO (ROCMPN, RODIVN, ROLOCN, ROORDN, RORTEN, RODSPD, ROSTOP) " +
      "VALUES(?, ?, ?, ?, ?, ?, ?)";

   public static final String OP_RouteHistory_Order_Delete =
      "DELETE FROM OPERATIONS.LRTHSTO " +
      "WHERE ROCMPN = ? AND RODIVN = ? AND ROLOCN = ? AND ROORDN = ?";
      
   public static final String OP_RouteHistory_Retrieve_DispatchDateRange = 
      "SELECT RHRTEN as Route, RHDSPD as DispatchDate, RHPDPD as PlannedRouteDepartDate, RHPDPT as PlannedRouteDepartTime, " +
      "       RHADPD as ActualRouteDepartDate, RHADPT as ActualRouteDepartTime, RHPRTD as PlannedRouteReturnDate, " +
      "       RHPRTT as PlannedRouteReturnTime, RHARTD as ActualRouteReturnDate, RHARTT as ActualRouteReturnTime, " +
      "       RHPMIL as PlannedMiles, RHAMIL as ActualMiles, RHRDCS as RoadnetCustomerID, RHDEPO as DepotOriginID, " +
      "       RDCMPN as Company, RDDIVN as Division, RDLOCN as Location, " + 
      "       RDSTOP as StopNumber, RDLCID as LocationID, RDATYP as ActivityType, RDPCS as Pieces, RDWGT as Weight, " +
      "       RDCUB as Cube,  RDPADT as PlannedArrivalDate, RDPATM as PlannedArrivalTime, RDPDDT as PlannedDepartDate, " +
      "       RDPDTM as PlannedDepartTime, RDAADT as ActualArrivalDate, RDAATM as ActualArrivalTime, RDADDT as AcutalDepartDate, " +
      "       RDADTM as ActualDepartTime, RDCTYP as CollectionType, RDKEYD as KeyDrop, RDPDST as DistanceFromPrevStop, " +
      "       RDWND1 as Windows1, RDWND2 as Windows2, RDWND3 as Windows3, RDOTRS as OnTimeResult, RDCMNT as Comments, " +
	  " 	  RDNORC as NotOnTimeReason " +	  
      "FROM OPERATIONS.LRTHSTH2 LEFT JOIN OPERATIONS.LRTHSTD1 ON (RHCMPN = RDCMPN) AND (RHDIVN = RDDIVN) AND " +
      "                                                     (RHLOCN = RDLOCN) AND (RHDSPD = RDDSPD) AND " +
      "                                                     (RHRTEN = RDRTEN) " +
      //"WHERE RHCMPN = ? AND RHDIVN = ? AND RHLOCN = ? AND (RHDSPD BETWEEN ? AND ?) " +
      "WHERE RHRDCS = ? AND (RHDSPD BETWEEN ? AND ?) " +
      "ORDER BY RHRTEN, RDSTOP";
      
   public static final String OP_RouteHistory_Header_Update = 
     "UPDATE OPERATIONS.LRTHSTH2 SET RHADPD = ?, RHADPT = ?, RHARTD = ?, RHARTT = ?, RHAMIL = ?, RHUSRM = ?,  " +
     "                              RHDLM = ?, RHTLM = ? " +
     "WHERE RHRDCS = ? AND RHDSPD = ? AND RHRTEN = ? ";                   

   public static final String OP_RouteHistory_Detail_Update = 
     "UPDATE OPERATIONS.LRTHSTD SET RDAADT = ?, RDAATM = ?, RDADDT = ?, RDADTM = ?, RDCTYP = ?, RDKEYD = ?, RDWND1 = ?, " +
     "                              RDWND2 = ?, RDWND3 = ?, RDOTRS = ?, RDCMNT = ?, RDUSRM = ?, RDDLM = ?, RDTLM = ?, RDNORC = ? " +
     "WHERE RDCMPN = ? AND RDDIVN = ? AND RDLOCN = ? AND RDRTEN = ? AND RDDSPD = ? AND RDSTOP = ?";  
   
   public static final String OP_CurrentDeliveryList = 
     "SELECT RDLCID as LocationID " +
     "FROM OPERATIONS.LRTHSTD1 " +
     "WHERE RDCMPN = ? AND RDDIVN = ? AND RDLOCN = ? AND RDDSPD = ? AND RDATYP = 'O'" +
     "ORDER BY RDLCID";

   public static final String OP_RemoveExistingDelivery = 
       "DELETE FROM OPERATIONS.LRTHSTD2 " +
       "WHERE RDCMPN = ? AND RDDIVN = ? AND RDLOCN = ? AND RDLCID = ? AND RDDSPD = ?";
   

   public static final String OP_DardenItems = 
     "SELECT CIITMN AS ItemNumber, CINWGT AS CaseWeight, CIPITM AS ParentItemNumber, FICBEC AS CaseCube " +
     "FROM HSUSER.CITCHNL3 JOIN HSDATA.FIITMAL0 " +
     "  ON (CICMPN = FICMPN) AND (CIITMN = FIITMN) " +
     "WHERE CICMPN = ? AND CIDIVN = ? AND CIDPTN = ? AND CIPRNT = ? " +
     "GROUP BY CIITMN, CINWGT, CIPITM, FICBEC " +
     "ORDER BY CIITMN";
   
   
   /********************************************************************************************************************
    * SQL statements used by OBC classes   
    ********************************************************************************************************************/
   public static final String OBC_History_Routes = 
      "SELECT TRIP_CID as TripIdentifier, TRIP_RouteID AS Route, TRIP_TripID as TripID, TRIP_BeginTs AS StartTime, " +
      "       TRIP_EndTs as EndTime, TRIP_TotalMiles as Miles " +  
      "FROM TRIP " +
      "WHERE FL_ID = ? AND " +
      "      (TRIP_BeginTs Between ? And ?) AND " +
      "      (TRIP_Valid = 'Y') AND" +
      "      (TRIP_RouteID Is Not Null)  " +
      "ORDER BY TRIP_RouteID;";
      
   public static final String OBC_History_Details =
      "SELECT  TRIP_ACTIVITIES.AH_ID1 AS LocationID, TRIP_ACTIVITIES.AH_Ts AS ArrivalTime, TRIP_ACTIVITIES_1.AH_Ts AS DepartTime " +
      "FROM    TRIP_ACTIVITIES LEFT OUTER JOIN TRIP_ACTIVITIES TRIP_ACTIVITIES_1 ON " +
      "           TRIP_ACTIVITIES.TRIP_CID = TRIP_ACTIVITIES_1.TRIP_CID AND " + 
      "           TRIP_ACTIVITIES.cKey = TRIP_ACTIVITIES_1.AH_LastLocIDcKey " +
      "WHERE     (TRIP_ACTIVITIES.TRIP_CID = ?) AND (TRIP_ACTIVITIES.AH_ActivityCode = 'ALO') AND (TRIP_ACTIVITIES_1.AH_ActivityCode = 'ACG') AND " +
      "          (TRIP_ACTIVITIES.AH_ID1 Is Not Null) AND (TRIP_ACTIVITIES.AH_ID1 <> '0000000000')";
   
   
   // No longer needed - now stored in Settings
   /*
   public static final String OBC_FleetID =
       "SELECT FL_ID as FleetID " +
       "FROM FLEETS " +
       "WHERE FL_ID <> 0";
   */
   
   public static final String OBC_DispatchRoutes = 
       "SELECT DPH_RouteID as RouteID " +
       "FROM DISP_HEADER " +
       "WHERE FL_ID = ? AND " +
       "      DPH_Type = 'T' AND " +
       "      DPH_TripID = ? " +
       "ORDER BY DPH_RouteID";

   
   /********************************************************************************************************************
    * SQL statements used by Settings
    ********************************************************************************************************************/
   public static final String ST_Regions = 
      "SELECT Region_ID as RegionID " +
      "FROM TSDBA.TS_REGION " +
      "ORDER BY REGION_ID";
   
   /********************************************************************************************************************
    * SQL statements used by Reports
    ********************************************************************************************************************/
   public static final String RPT_WeeklyOnTimeDetail = 
       "SELECT RHRDCS AS Region, RDCMPN as Company, RDDSPD as DispatchDate, RDRTEN as Route, RDSTOP as StopNumber, " +
       "       RDLCID as RoutingLocationID, RDPADT as RoutedArriveDate, RDPATM as RoutedArriveTime, RDAADT as ActualArriveDate, " +
       "       RDAATM as ActualArriveTime, RDCTYP as CollectionType, RDKEYD as KeyDropFlag, RDWND1 as Windows1, " +
       "       RDWND2 as Windows2, RDOTRS as OnTimeResult, RDCMNT as Comments, FDCNMB as CustomerName, FDCTYB as City, FDSTEB as State, " +
       "       BSCSCD as ChainCode, BSCTDC as ChainDescription " + 
       "FROM OPERATIONS.LRTHSTH2 INNER JOIN (OPERATIONS.LRTHSTD LEFT JOIN (HSDATA.FDCSTBL0 LEFT JOIN HSDATA.BSCHNHL0 ON (FDCSCD = BSCSCD) AND " +
       "                                                                                                                (FDDPTN = BSDPTN) AND " +
       "                                                                                                                (FDDIVN = BSDIVN) AND " +
       "                                                                                                                (FDCMPN = BSCMPN)) ON " +
       "                                                                  (RDLCID = TRIM(FDCUSN)) AND (RDLOCN = FDDPTN) AND " +
       "                                                                  (RDDIVN = FDDIVN) AND (RDCMPN = FDCMPN)) ON " +
       "                                    (RHDSPD = RDDSPD) AND (RHRTEN = RDRTEN) AND (RHLOCN = RDLOCN) AND" +
       "                                    (RHDIVN = RDDIVN) AND (RHCMPN = RDCMPN) " +
       "WHERE RHRDCS = ? AND (RDDSPD Between ? And ?) AND RDATYP = 'O' " +
       "ORDER BY BSCSCD, RDLCID, RDDSPD";
}

/****************************************************************************************************************
 * Modifications
 *
 * 2004-03-30 Added OD_OrderCount
 * 2004-04-01 Added ST_Regions, OD_OrdersSentToRouting
 * 2004-04-02 Added OD_SkeletonRoutes, OD_SkeletonRoutes_Detail
 * 2004-04-16 Added OS_SkeletonRoutes, OS_CallSchedule, OS_ClearCallSchedule,
 *                  OS_UpdateCallSchedule, LS_LookupSuspendedStatus
 * 2004-05-19 Added FDCNMB to RM_HostCustomerData
 * 2004-05-26 Added OP_BackhaulAppointments
 * 2004-06-14 Altered OD_OrderData to exclude X-trips (customer pickups)
 * 2004-06-19 Added OP_BackhaulAppointments_UpdateRouting, OP_RouteHistory_Header_Insert,
 *                  OP_RouteHistory_Detail_Insert, OP_RouteHistory_Detail_Order,
 *                  OP_RouteHistory_Header_Delete, OP_RouteHistory_Detail_Delete,
 *                  OP_RouteHistory_Order_Delete
 * 2004-06-21 Added OBC_History_Routes, OBC_History_Details
 * 2004-06-24 Added OP_RouteHistory_Retrieve_DispatchDate
 * 2004-06-30 Changed OP_RouteHistory_Retrieve_DispatchDate to OP_RouteHistory_Retrieve_DispatchDateRange
 *            Added OP_RouteHistory_Header_Update, OP_RouteHistory_Detail_Update
 * 2004-07-05 Added OD_Override_OIORDDL0_REPORT, OD_Override_OIORDDL0_DAYEND and OD_Override_OHORDHL0_REPORT
 * 2004-07-21 Added OBC_FleetID, OBC_DispatchRoutes
 * 2004-07-29 Added OD_SkeletonRoutes_Details (replaced two other skeleton queries)
 * 2004-08-04 All Roadnet related queries had to be updated to accomodate the table and field name changes
 *             made in RoadNet 3.0.
 * 2004-09-10 Added RU_InsertRoute and RU_UpdateRoute
 * 2004-09-17 Updated OP_RouteHistory_Retrieve_DispatchDateRange to be a left join, for routes that have
 *             just a header and no detail
 * 2004-11-11 Added LS_AddedHostAccounts_All
 * 2004-11-17 Altered OP_RouteHistory_Retrieve_DispatchDateRange to pull by the RoadNet customer ID rather
 *             than by co-div-locn.  This is to solve the problem of dealing with companies who route multiple
 *             companies onto single routes (i.e. Chicago).
 * 2004-04-11 Altered OP_DardenItems to group on the item number, as multiple records could exist if the item
 *             is used in different Darden concepts.  Also started using the appropriate field for the weight
 * 2005-05-25 Added OP_CurrentDeliveryList
 * 2005-06-15 Altered OP_DardenItems to include case cubes from FIITM
 * 2005-06-27 Added RPT_WeeklyOnTimeDetail
 * 2005-06-29 Altered OP_RouteHistory_Header_Update to select via region instead of Co-Div-Loc
 *            Altered OP_RouteHistory_Retrieve_DispatchDateRange to pull co-div-loc info
 * 2005-11-13 Removed OBC_FleetID.  The fleet ID is now stored in the settings
 *            Added FL_ID to OBC_History_Routes
 *            Added FL_ID to OBC_DispatchRoutes
 * 2006-08-03 Added OD_SetOrderInUseFlag
 * 2006-08-06 Updated RU_UpdateHostOrderHeader to clear out "in-use" flags
 * 2007-03-29 Updated RM_RouteSet_Locations to include a SEQUENCE_NUMBER >= 0 - filter out the layovers
 ****************************************************************************************************************/


