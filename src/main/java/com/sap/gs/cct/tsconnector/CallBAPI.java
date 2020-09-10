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
public class CallBAPI {
	private Logger logger = LoggerFactory.getLogger(CallBAPI.class);

	@GetMapping(path="/bapi", produces = "application/json; charset=UTF-8")
	public String getAssignments() {

		try {

			logger.debug("----> started");

			JCoDestination destination = JCoDestinationManager.getDestination("SAP_ECC");
			logger.debug("----> got destination: {}", destination.getDestinationName() );

			JCoRepository repo = destination.getRepository();
			logger.debug("----> got repo: {}", repo.getName() );
			
			JCoFunction stfcConnection = repo.getFunction("STFC_CONNECTION");
			logger.debug("----> got connection: {}", stfcConnection.getName() );

			JCoParameterList imports = stfcConnection.getImportParameterList();
			imports.setValue("REQUTEXT", "SAP Cloud Platform Connectivity runs with JCo");
			stfcConnection.execute(destination);

			JCoParameterList exports = stfcConnection.getExportParameterList();

			return exports.toString();

		} catch (AbapException abapException) {
			logger.error("----> abap exception: {}", abapException);
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, abapException.getMessageText(),
					abapException);

		} catch (JCoException jcoException) {
			logger.error("----> jcoException exception: {}", jcoException);
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, jcoException.getMessageText(),
					jcoException);

		} catch (Exception e) {
			logger.error("----> generic exception: {}", e);
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Exception", e);
		}

	}
}