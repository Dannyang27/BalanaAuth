package src.balanaauth;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Scanner;
import org.wso2.balana.Balana;
import org.wso2.balana.Indenter;
import org.wso2.balana.PDP;
import org.wso2.balana.PDPConfig;
import org.wso2.balana.ctx.ResponseCtx;
import org.wso2.balana.finder.impl.FileBasedPolicyFinderModule;

public class BalanaAuth {
    private static final String pathToRequestFile = "src/resources/request/XACMLRequest";
    private static final String pathToSaveFile = "src/resources/outputs/XACML";
    
    private static Balana balana;

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Seleccione el Request deseado [1,5]");
        int requestNumber = scanner.nextInt();
        
        initBalana();
        PDP pdp = getPDPNewInstance();        
        
        String requestFile = pathToRequestFile + requestNumber + ".xml";
        String response = pdp.evaluate(Utils.getXMLFromFilePath(requestFile));
        ResponseCtx responseCtx = ResponseCtx.getInstance(Utils.getXacmlResponse(response));

        String pathToSave = pathToSaveFile + "PolicyRequest" + requestNumber;
        OutputStream outputStream = new FileOutputStream(pathToSave);
        Utils.printResult(outputStream, new Indenter(), responseCtx.getResults());
    }

    private static PDP getPDPNewInstance(){
        PDPConfig pdpConfig = balana.getPdpConfig();
        return new PDP(new PDPConfig(pdpConfig.getAttributeFinder(), pdpConfig.getPolicyFinder(), null, true));
    }
    
    private static void initBalana(){
        try {
            String policyLocation = (new File(".")).getCanonicalPath() + File.separator + "src" + File.separator + "resources" + File.separator + "policy";
            System.setProperty(FileBasedPolicyFinderModule.POLICY_DIR_PROPERTY, policyLocation);
        } catch (IOException e) {
            System.err.println("Can not locate policy repository");
        }
        balana = Balana.getInstance();
    }
}
