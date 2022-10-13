import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.*;
import net.sourceforge.htmlunit.corejs.javascript.EcmaError;
import net.sourceforge.htmlunit.corejs.javascript.EvaluatorException;

import java.io.IOException;
import java.util.List;
import java.io.FileWriter;
import java.util.logging.Level;

public class Main {
    public static void main (String[] args) {
        WebClient webClient = new WebClient(BrowserVersion.CHROME);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setPrintContentOnFailingStatusCode(false);
        java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(Level.OFF);

        try {
            HtmlPage page = webClient.getPage("https://www.comparetv.com.au/streaming-search-library/?ctvcp=1770");
            FileWriter shows = new FileWriter("shows.csv", true);
            shows.write("\"Title\",\"URL\"\n");

            int count = 0;
            while (true) {
                HtmlDivision loadMoreButton = page.getFirstByXPath("//div[@class='ctv-load-more']");
                page = loadMoreButton.click();
                webClient.waitForBackgroundJavaScript(4000);
                int newCount = page.getByXPath("//div[@class='search-content-item']").size();
                if (newCount == count) {
                    break;
                } else {
                    count = newCount;
                }
                System.out.println("Shows found: " + count);
            }

            Thread.sleep(4000);

            System.out.println("Total Netflix shows: " + count);
            System.out.println("CSV output:");
            System.out.println("\"Title\",\"URL\"");

            List<?> anchors = page.getByXPath("//div[@class='search-content-item']");
            for (Object anchor : anchors) {
                HtmlDivision div = (HtmlDivision) anchor;
                HtmlAnchor a = (HtmlAnchor) div.getChildNodes().get(0);
                HtmlParagraph p = (HtmlParagraph) a.getChildNodes().get(1);

                String name = p.getFirstChild().asNormalizedText();
                String url = a.getHrefAttribute();

                String csv = "\"" + name + "\"" + "," + "\"" + url + "\"\n";
                System.out.print(csv);
                shows.write(csv);
            }

            shows.close();

            webClient.getCurrentWindow().getJobManager().removeAllJobs();
            webClient.close();
        } catch (IOException e) {
            System.out.println("An error occurred: " + e);
        } catch (EcmaError | EvaluatorException e) {
            System.out.println("ERROR");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
