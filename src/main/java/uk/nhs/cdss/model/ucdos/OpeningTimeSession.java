package uk.nhs.cdss.model.ucdos;

import lombok.Data;

@Data
public class OpeningTimeSession {
  private OpeningTime start;
  private OpeningTime end;
}
