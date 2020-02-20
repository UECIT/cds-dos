package uk.nhs.cdss.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uk.nhs.cdss.model.InputBundle;
import uk.nhs.cdss.model.ucdos.ClinicalTermSearch;
import uk.nhs.cdss.model.ucdos.ServiceTypeSearch;
import uk.nhs.cdss.model.ucdos.wsdl.CheckCapacitySummary;
import uk.nhs.cdss.transform.ucdos.in.CheckCapacityResponseTransformer;
import uk.nhs.cdss.transform.ucdos.in.DosRestResponseTransformer;
import uk.nhs.cdss.transform.ucdos.out.CheckCapacitySearchTransformer;
import uk.nhs.cdss.transform.ucdos.out.ClinicalTermSearchTransformer;
import uk.nhs.cdss.transform.ucdos.out.ServiceTypeSearchTransformer;

@Slf4j
@Service
@AllArgsConstructor
public class UCDOSService {

  private ServiceTypeSearchTransformer serviceTypeSearchTransformer;
  private ClinicalTermSearchTransformer clinicalTermSearchTransformer;
  private CheckCapacitySearchTransformer checkCapacitySearchTransformer;
  private CheckCapacityResponseTransformer checkCapacityResponseTransformer;
  private DosRestResponseTransformer dosRestResponseTransformer;

  public void invokeUCDOS(InputBundle inputBundle) {

    ServiceTypeSearch serviceTypeSearch = serviceTypeSearchTransformer.transform(inputBundle);
    ClinicalTermSearch clinicalTermSearch = clinicalTermSearchTransformer.transform(inputBundle);
    CheckCapacitySummary checkCapacitySearch = checkCapacitySearchTransformer.transform(inputBundle);

    log.info("ServiceType Search: {}", serviceTypeSearch);
    log.info("ClinicalTerm Search: {}", clinicalTermSearch);
    log.info("CheckCapacitySummary Search: {}", serialiseXml(checkCapacitySearch));

//    DosRestResponse dosRestResponse = null;
//    Parameters restServices = dosRestResponseTransformer.transform(dosRestResponse);
//    log.info("ServiceType/ClinicalTerm Response: from {} to {}", dosRestResponse, restServices);
//
//    CheckCapacitySummaryResponse checkCapacityResponse = null;
//    Parameters soapServices = checkCapacityResponseTransformer.transform(checkCapacityResponse);
//    log.info(
//        "CheckCapacitySummary Response: from {} to {}",
//        serialiseXml(checkCapacityResponse),
//        soapServices);
  }

  private static String serialiseXml(Object data) {
    try {
      return new XmlMapper().writeValueAsString(data);
    } catch (JsonProcessingException e) {
      return "[failed to serialise]";
    }
  }
}
