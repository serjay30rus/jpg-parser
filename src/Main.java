import org.apache.commons.io.FilenameUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.net.MalformedURLException;

import java.net.URL;
import java.nio.file.*;
import java.util.List;

import static java.nio.file.Files.copy;
import static java.nio.file.Files.probeContentType;

public class Main {
    public static void main(String[] args) throws IOException {

        final String url = "http://lenta.ru/";  // Creating of new variable with URL for download
        final String targetFolder = "out/images";   // Destination path
        ImageValidator imageValidator = new ImageValidator();

        try {
            Document doc = Jsoup
                    .connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36")
                    .get();

            doc.select("img").stream()
                    .map(it -> it.attr("src"))
                    .filter(imageValidator::validate)
                    .forEach(e -> {
                try {
                    URL imgUrl = new URL(e);
                    String fileName = FilenameUtils.getName(imgUrl.getPath());
                    File localFile = new File(targetFolder, fileName);

                    FileUtils.copyURLToFile(imgUrl, localFile);
                    System.out.println("Successfully saved picture with src attribute = " + e);
                } catch (Exception ex) {
                    System.out.println("Not valid picture with src attribute = " + e);
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
