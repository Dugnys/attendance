package com.dugnys.labora4;

import com.dugnys.attendance.GroupTable;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.scene.control.TableView;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class FileHandler {

    private FileHandler(){}

    public static void exportCSV(String filePath, TableView table) throws Exception {
        //TableView with hashMap
        Writer writer;
        filePath = filePath + "\\output.csv";
        File file = new File(filePath);

        StringBuilder stringBuilder = new StringBuilder();

        HashMap<String, String> map = (HashMap) table.getItems().get(0);
        ArrayList<String> sortedKeys = new ArrayList<>();
        for (Map.Entry<String, String> entry: map.entrySet())
            sortedKeys.add(entry.getKey());

        Collections.sort(sortedKeys, new Comparator<String>() {
            public int compare(String s1, String s2) {
                boolean s1IsWord = s1.matches("[a-zA-Z]+");
                boolean s2IsWord = s2.matches("[a-zA-Z]+");
                if (s1IsWord && !s2IsWord) {
                    return -1;
                } else if (!s1IsWord && s2IsWord) {
                    return 1;
                } else if (s1IsWord && s2IsWord) {
                    return s1.compareTo(s2);
                } else {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    LocalDate d1 = LocalDate.parse(s1, formatter);
                    LocalDate d2 = LocalDate.parse(s2, formatter);
                    return d1.compareTo(d2);
                }
            }
        });

        for (String key: sortedKeys)
            stringBuilder.append(key + ", ");
        stringBuilder.append('\n');

        for (int i = 0; i < table.getItems().size(); i++) {
            HashMap<String, String> mapi = (HashMap) table.getItems().get(i);
            for(String key: sortedKeys) {
                if (mapi.get(key) != "") {
                    stringBuilder.append(mapi.get(key) + ", ");
                } else {
                    stringBuilder.append(",");
                }
            }
            stringBuilder.append('\n');
        }

        writer = new BufferedWriter(new FileWriter(file));
        writer.write(stringBuilder.toString());

        writer.flush();
        writer.close();
    }

    public static void exportXLS(String filePath, TableView table) throws Exception {
        Workbook workbook = new HSSFWorkbook();
        Sheet spreadsheet = workbook.createSheet("sheet1");

        Row row = spreadsheet.createRow(0);

        HashMap<String, String> map = (HashMap) table.getItems().get(0);
        ArrayList<String> sortedKeys = new ArrayList<>();
        for (Map.Entry<String, String> entry: map.entrySet())
            sortedKeys.add(entry.getKey());

        Collections.sort(sortedKeys, new Comparator<String>() {
            public int compare(String s1, String s2) {
                boolean s1IsWord = s1.matches("[a-zA-Z]+");
                boolean s2IsWord = s2.matches("[a-zA-Z]+");
                if (s1IsWord && !s2IsWord) {
                    return -1;
                } else if (!s1IsWord && s2IsWord) {
                    return 1;
                } else if (s1IsWord && s2IsWord) {
                    return s1.compareTo(s2);
                } else {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    LocalDate d1 = LocalDate.parse(s1, formatter);
                    LocalDate d2 = LocalDate.parse(s2, formatter);
                    return d1.compareTo(d2);
                }
            }
        });

        int ii=0;
        for (String key: sortedKeys){
            row.createCell(ii).setCellValue(key);
            ii++;
        }

        for (int i = 0; i < table.getItems().size(); i++) {
            row = spreadsheet.createRow(i + 1);
            HashMap<String, String> mapi = (HashMap) table.getItems().get(i);
            int j =0;
            for (String key: sortedKeys) {
                if (mapi.get(key) != "") {
                    row.createCell(j).setCellValue(mapi.get(key));
                } else {
                    row.createCell(j).setCellValue(" ");
                }
                j++;
            }
        }


        filePath = filePath + "\\output.xls";
        FileOutputStream fileOut = null;

        File old = new File(filePath);
        old.delete();
        fileOut = new FileOutputStream(filePath);

        workbook.write(fileOut);
        fileOut.close();

    }

    public static GroupTable importCSV(String filePath) throws Exception {
        GroupTable table = new GroupTable();
        File file = new File(filePath);
        Scanner lineScanner = new Scanner(file);
        List<String> list = new ArrayList<>();

        lineScanner.useDelimiter("\n");
        while (lineScanner.hasNext()) {
            String temp = lineScanner.next();
            temp = temp.replace(" ", "");
            if(temp != "")
                list.add(temp);
        }

        String[] items = list.get(0).split(",", 0);
        for (String item: items) {
            table.addNewColumn(item);
        }

        for (int i=1; i<list.size(); i++){
            items = list.get(i).split(",", -1);
            Map<String, String> row = new HashMap<>();

            for (int j=0; j<table.keys.size(); j++) {
                row.put(table.keys.get(j), items[j]);//
            }

            table.getTable().getItems().add(row);
        }

        return table;
    }

    public static GroupTable importXLS(String filePath) throws Exception {
        GroupTable table = new GroupTable();

            InputStream inputStream = new FileInputStream(filePath);
            DataFormatter formatter = new DataFormatter();

            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheetAt(0);

            Row headRow = sheet.getRow(0);
            Map<String, String> headMap = new HashMap<>();
            for (Cell cell: headRow){
                String text = formatter.formatCellValue(cell);
                table.addNewColumn(text);
            }

            for (int i=1; i<=sheet.getLastRowNum(); i++) {
                Map<String, String> tempMap = new HashMap<>();
                Row row = sheet.getRow(i);

                for (int j=0; j<table.keys.size(); j++) {
                    Cell cell = row.getCell(j);
                    String text = formatter.formatCellValue(cell);
                    tempMap.put(table.keys.get(j), text);

                }
                table.getTable().getItems().add(tempMap);
            }

        return table;
    }

    public static void exportPDF(String filePath, TableView table) throws Exception{
        filePath = filePath + "\\output.pdf";
        File file = new File(filePath);

        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(file));
        document.open();

        HashMap<String, String> map = (HashMap) table.getItems().get(0);
        ArrayList<String> sortedKeys = new ArrayList<>();
        for (Map.Entry<String, String> entry: map.entrySet())
            sortedKeys.add(entry.getKey());

        Collections.sort(sortedKeys, new Comparator<String>() {
            public int compare(String s1, String s2) {
                boolean s1IsWord = s1.matches("[a-zA-Z]+");
                boolean s2IsWord = s2.matches("[a-zA-Z]+");
                if (s1IsWord && !s2IsWord) {
                    return -1;
                } else if (!s1IsWord && s2IsWord) {
                    return 1;
                } else if (s1IsWord && s2IsWord) {
                    return s1.compareTo(s2);
                } else {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    LocalDate d1 = LocalDate.parse(s1, formatter);
                    LocalDate d2 = LocalDate.parse(s2, formatter);
                    return d1.compareTo(d2);
                }
            }
        });

        PdfPTable pdfPTable = new PdfPTable(sortedKeys.size());
        for (String key: sortedKeys)
            pdfPTable.addCell(key);

        for (int i = 0; i < table.getItems().size(); i++) {
            HashMap<String, String> mapi = (HashMap) table.getItems().get(i);
            for(String key: sortedKeys) {
                if (mapi.get(key) != "") {
                    pdfPTable.addCell(mapi.get(key));
                } else {
                    pdfPTable.addCell("");
                }
            }
        }

        document.add(pdfPTable);
        document.close();
    }

}
