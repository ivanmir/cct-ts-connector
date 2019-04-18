package com.sap.gs.cct.tsconnector;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.sap.conn.jco.AbapException;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoParameterList;
import com.sap.conn.jco.JCoRepository;
import com.sap.conn.jco.JCoTable;

@RestController
public class AdminData {
	// private Logger logger = LoggerFactory.getLogger(AdminData.class);

	@GetMapping(value = "assignments/{userId}", produces = "application/json; charset=UTF-8")
	public String getAssignments(@PathVariable("userId") String userId) {

		try {

			// logger.info("started");

			String current = new java.io.File(".").getCanonicalPath();
			System.out.println("Current dir:" + current);

			JCoDestination destination = JCoDestinationManager.getDestination("CoDeX_B7W");

			JCoRepository repo = destination.getRepository();
			JCoFunction stfcConnection = repo.getFunction("STFC_CONNECTION");

			JCoParameterList imports = stfcConnection.getImportParameterList();
			imports.setValue("REQUTEXT", "SAP Cloud Platform Connectivity runs with JCo");
			stfcConnection.execute(destination);

			if (userId == null) {
				throw new IllegalArgumentException("User ID missing");
			}

			JCoFunction adminGet = repo.getFunction("ZTEST_UID_ADMIN_GET");

			JCoParameterList adminGetParameters = adminGet.getImportParameterList();

			adminGetParameters.setValue("USER_ID", userId);

			adminGet.execute(destination);

			JCoParameterList klausExports = adminGet.getExportParameterList();
			JCoTable tab = klausExports.getTable("T_ADMIN_DATA");

			return tab.toJSON();

		} catch (AbapException abapException) {

			// logger.error("abap exception", abapException);
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, abapException.getMessageText(),
					abapException);

		} catch (JCoException jcoException) {

			// logger.error("jcoException exception", jcoException);
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, jcoException.getMessageText(),
					jcoException);

		} catch (Exception e) {
			// logger.error("exception", e);
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Exception", e);
		}

	}
}