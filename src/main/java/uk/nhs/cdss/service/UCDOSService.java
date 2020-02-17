package uk.nhs.cdss.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uk.nhs.cdss.model.CheckServicesInputBundle;
import uk.nhs.cdss.model.ucdos.ClinicalTermSearch;
import uk.nhs.cdss.model.ucdos.ServiceTypeSearch;
import uk.nhs.cdss.model.ucdos.wsdl.CheckCapacitySummary;
import uk.nhs.cdss.transform.ucdos.CheckCapacitySearchTransformer;
import uk.nhs.cdss.transform.ucdos.ClinicalTermSearchTransformer;
import uk.nhs.cdss.transform.ucdos.ServiceTypeSearchTransformer;

@Slf4j
@Service
@AllArgsConstructor
public class UCDOSService {

  private ServiceTypeSearchTransformer serviceTypeSearchTransformer;
  private ClinicalTermSearchTransformer clinicalTermSearchTransformer;
  private CheckCapacitySearchTransformer checkCapacitySearchTransformer;

  public void invokeUCDOS(CheckServicesInputBundle inputBundle) {

    ServiceTypeSearch serviceTypeSearch = serviceTypeSearchTransformer.transform(inputBundle);
    ClinicalTermSearch clinicalTermSearch = clinicalTermSearchTransformer.transform(inputBundle);
    CheckCapacitySummary checkCapacitySearch = checkCapacitySearchTransformer.transform(inputBundle);

    log.info("ServiceType Search: {}", serviceTypeSearch);
    log.info("ClinicalTerm Search: {}", clinicalTermSearch);
    try {
      log.info("CheckCapacitySummary Search: {}",
          new XmlMapper().writeValueAsString(checkCapacitySearch));
    } catch (JsonProcessingException e) {
      log.error("Failed to serialise CheckCapacitySummary Search");
    }
  }

}
