package tests.mdm.customer;

import com.uscold.ui.pageobject.customer.CustomerManagementPage;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import tests.BaseTest;
import tests.util.Helper;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * ***********************************************************************************************************************
 * Uscold.MDM
 * User: fwrmoral
 * 2/24/19
 * ***********************************************************************************************************************
 **/

public class CreateNewCustomer extends BaseTest {

    CustomerManagementPage customerManagementPage = PageFactory.initElements(driver,CustomerManagementPage.class);

    @Test(priority = 1, dataProvider = "LabelMappingData",description = "Simple test")
    public void customerCreateNewTest(String labelForm, String labelType){
        customerManagementPage.navigateToCustomerManagementPage(driver);
        Helper.click(customerManagementPage.clickOnCreateNewButton(driver));

    }

    @DataProvider()
    public Object[][] LabelMappingData() {
        Object data[][] = Helper.getLabelMappingData(TEST_DATA+"/excel/label/Label.xlsx","Sheet1");
        return data;
    }
}
