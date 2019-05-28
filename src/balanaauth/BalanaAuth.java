package src.balanaauth;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import org.wso2.balana.Balana;
import org.wso2.balana.Indenter;
import org.wso2.balana.PDP;
import org.wso2.balana.PDPConfig;
import org.wso2.balana.ctx.ResponseCtx;
import org.wso2.balana.finder.impl.FileBasedPolicyFinderModule;

public class BalanaAuth {
    private static final String pathToRequestFile = "src/resources/request/XACMLRequest";
    private static Balana balana;

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Seleccione el Request deseado [1,5]");
        int requestNumber = scanner.nextInt();

        try {
            String policyLocation = (new File(".")).getCanonicalPath() + File.separator + "src" + File.separator + "resources" + File.separator + "policy";
            System.setProperty(FileBasedPolicyFinderModule.POLICY_DIR_PROPERTY, policyLocation);
        } catch (IOException e) {
            System.err.println("No existe la politica");
        }

        balana = Balana.getInstance();

        PDPConfig pdpConfig = balana.getPdpConfig();
        PDP pdp = new PDP(new PDPConfig(pdpConfig.getAttributeFinder(), pdpConfig.getPolicyFinder(), null, true));

        String requestFile = pathToRequestFile + requestNumber + ".xml";
        String response = pdp.evaluate(Utils.getXMLFromFilePath(requestFile));

        ResponseCtx responseCtx = ResponseCtx.getInstance(Utils.getXacmlResponse(response));
        Utils.printResult(System.out, new Indenter(), responseCtx.getResults());
    }
}
