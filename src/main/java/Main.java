import metro.Line;
import metro.Station;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static final File htmlMetroFile = new File("data/metro.html");
    private static List<Station> stations = new ArrayList<>();
    private static List<Line> lines = new ArrayList<>();
    private static JSONObject metroJson = new JSONObject();

    public static void main(String[] args) {
        try {
            BufferedWriter jsonFileSave = Files.newBufferedWriter(Paths.get("data/metro.json"));
            parseLines();
            parseStations();
            metroJson.put("lines", createJsonLines(lines));
            metroJson.put("stations", createJsonStation(stations));
            jsonFileSave.write(metroJson.toJSONString());
            jsonFileSave.close();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // parse MSK Metro html file
    private static Document downloadHtml() throws IOException {
        return Jsoup.parse(htmlMetroFile, StandardCharsets.UTF_8.name());
    }

    // parse and add Lines
    private static void parseLines() throws IOException {
        downloadHtml().body().select("span.js-metro-line.t-metrostation-list-header")
                .stream()
                .map(e -> {
                    var id = findDataLine(e.toString());
                    var name = e.text();
                    return lines.add(new Line(id,name));
                })
                .toList();
    }

    // parse data-line in string
    private static String findDataLine(String text) {
        String temp1 = text.substring(text.lastIndexOf("d"), text.lastIndexOf("\"") + 1);
        return temp1.substring(temp1.indexOf("\"") + 1, temp1.lastIndexOf("\"")).trim();
    }

    // parse and add Stations
    private static void parseStations() throws IOException {
        Elements stationElements = downloadHtml().body().select("div.js-metro-stations.t-metrostation-list-table");

        for (Element element : stationElements) {
            var idLine = element.attr("data-line");
            Elements nameStationElement = element.select("span.name");
            for (Element elementStation : nameStationElement) {
                var name = elementStation.text();
                stations.add(new Station(name, idLine));
            }
        }
    }

    private static JSONArray createJsonLines(List<Line> lines) {
        JSONArray jsonArrayLines = new JSONArray();

        for (Line line : lines) {
            JSONObject objectLine = new JSONObject();
            objectLine.put("number", line.getId());
            objectLine.put("name", line.getName());
            jsonArrayLines.add(objectLine);
        }
        return jsonArrayLines;
    }

    private static JSONArray createJsonStation(List<Station> stations) {
        JSONArray jsonArrayLines = new JSONArray();

        for (Station station : stations) {
            JSONObject objectLine = new JSONObject();
            objectLine.put("number", station.getIdLine());
            objectLine.put("name", station.getName());
            jsonArrayLines.add(objectLine);
        }
        return jsonArrayLines;
    }
}