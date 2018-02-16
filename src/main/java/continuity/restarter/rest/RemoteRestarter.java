package continuity.restarter.rest;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping(value = "restart")
public class RemoteRestarter {

	/**
	 * Restful endpoint to get traces, which are in the buffer as OPEN.xtrace.
	 * 
	 * @param fromDate
	 *            earliest date
	 * @param toDate
	 *            latest date
	 * @return JSON formatted openXTRACE
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	@RequestMapping(value = "/dvdstore", method = GET)
	@ResponseBody
	public ResponseEntity<String> restartDVDStore() {
		Process p1;
		Process p2;

		try {
			String startContainerCommand = "sudo service dvdstore restart";
			
			p2 = Runtime.getRuntime().exec(new String[] { "bash", "-c", startContainerCommand });
					
			p2.waitFor();
			String result = "";
			BufferedReader reader = new BufferedReader(new InputStreamReader(p2.getInputStream()));

			String line = "";
			while ((line = reader.readLine()) != null) {
				result += (line + "\n");
			}
			return new ResponseEntity<String>(result, HttpStatus.OK);
		} catch (IOException | InterruptedException e) {
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	/**
	 * Header information for swagger requests.
	 *
	 * @param response
	 *            Response information
	 */
	@ModelAttribute
	public void setVaryResponseHeader(HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");

	}
}
