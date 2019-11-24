package com.sap.gs.cct.tsconnector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.sap.conn.jco.AbapException;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoParameterList;
import com.sap.conn.jco.JCoRepository;

@RestController
public class AdminData {
	private Logger logger = LoggerFactory.getLogger(AdminData.class);

	@GetMapping(path="/assignments", produces = "application/json; charset=UTF-8")
	public String getAssignments() {

		try {

			logger.info("started");

			JCoDestination destination = JCoDestinationManager.getDestination("Kestraa-ECC");
			logger.info("got destination: " + destination.getDestinationName() );

			JCoRepository repo = destination.getRepository();
			logger.info("got repo: " + repo.getName() );
			
			JCoFunction stfcConnection = repo.getFunction("STFC_CONNECTION");
			logger.info("got connection: " + stfcConnection.getName() );

			JCoParameterList imports = stfcConnection.getImportParameterList();
			imports.setValue("REQUTEXT", "SAP Cloud Platform Connectivity runs with JCo");
			stfcConnection.execute(destination);

			JCoParameterList exports = stfcConnection.getExportParameterList();
			//String echotext = exports.getString("ECHOTEXT");
			//String resptext = exports.getString("RESPTEXT");

			return exports.toJSON();

			//JCoFunction adminGet = repo.getFunction("ZTEST_UID_ADMIN_GET");
			//JCoParameterList adminGetParameters = adminGet.getImportParameterList();
			//adminGetParameters.setValue("USER_ID", userId);
			//adminGet.execute(destination);
			//JCoParameterList klausExports = adminGet.getExportParameterList();
			//JCoTable tab = klausExports.getTable("T_ADMIN_DATA");

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