package uk.nhs.cdss.model.ucdos;

import lombok.Data;

@Data
public class OpeningTimeRota {
  private boolean openAllHours;
  private String day;
  private OpeningTimeSession session;
  private String specifiedDates;
}
