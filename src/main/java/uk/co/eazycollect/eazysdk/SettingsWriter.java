package uk.co.eazycollect.eazysdk;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Properties;

public class SettingsWriter {

    public SettingsWriter() {
        Properties settings = new Properties();
        settings.setProperty("currentEnvironment.Environment", "sandbox");
        settings.setProperty("sandboxClientDetails.ApiKey", "");
        settings.setProperty("sandboxClientDetails.ClientCode", "");
        settings.setProperty("ecm3ClientDetails.ApiKey", "");
        settings.setProperty("ecm3ClientDetails.ClientCode", "");
        settings.setProperty("directDebitProcessingDays.InitialProcessingDays", "10");
        settings.setProperty("directDebitProcessingDays.OngoingProcessingDays", "5");
        settings.setProperty("contracts.AutoFixStartDate", "false");
        settings.setProperty("contracts.AutoFixTerminationTypeAdHoc", "false");
        settings.setProperty("contracts.AutoFixAtTheEndAdHoc", "false");
        settings.setProperty("contracts.AutoFixPaymentDayInMonth", "false");
        settings.setProperty("contracts.AutoFixPaymentMonthInYear", "false");
        settings.setProperty("payments.AutoFixPaymentDate", "false");
        settings.setProperty("payments.IsCreditAllowed", "false");
        settings.setProperty("warnings.CustomerSearchWarning", "false");
        settings.setProperty("other.BankHolidayUpdateDays", "30");
        settings.setProperty("other.ForceUpdateSchedulesOnRun", "false");

        try(BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(Paths.get("./appSettings.xml").toFile()))) {
            settings.storeToXML(stream, "Configuration for EazyCollectSDK-Java");
        } catch(IOException e) {
            e.printStackTrace();
        }

    }

}
