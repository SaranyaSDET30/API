package utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class excelutils {
	
	public static FileInputStream fi;
	public static XSSFWorkbook workbook;
	public static XSSFSheet sheet;
	public static XSSFRow row;
	public static XSSFCell cell;
	public static String excelPath="./Testdata/Excel data.xlsx";
	public static String sheetName="Sheet1";
	
	
	
	public static String getCelldata(String excelPath, String sheetName,int rowNum,int columnNum) throws IOException {
		
		fi = new FileInputStream(excelPath);
		workbook = new XSSFWorkbook(fi);
		sheet = workbook.getSheet(sheetName);
	    row = sheet.getRow(rowNum);
		sheet = workbook.getSheet(sheetName);
		row = sheet.getRow(rowNum);
		cell = row.getCell(columnNum);
		String data; try {
		DataFormatter formatter=new DataFormatter();
		data=formatter.formatCellValue(cell);
		  }catch(Exception e) {
			data = "";
		}
		return data;
	}
		
		
			
	public static int getRowCount(String excelPath, String sheetName) throws IOException {
		
		fi = new FileInputStream(excelPath);
		workbook = new XSSFWorkbook(fi);
		sheet = workbook.getSheet(sheetName);
		int rowCount=sheet.getLastRowNum();
		System.out.println("No.of Rows:"+rowCount);
		return rowCount;
		}

	public static int getCellCount(String excelPath, String sheetName, int rowNum) throws IOException{
		
		fi = new FileInputStream(excelPath);
		workbook = new XSSFWorkbook(fi);
		sheet = workbook.getSheet(sheetName);
	    row = sheet.getRow(rowNum);
		int columnCount = row.getLastCellNum();
		return columnCount;
}
	
	public static void setCellData(String excelPath, String sheetName, int rowNum, int colNum, String data) throws IOException {
		
		workbook = new XSSFWorkbook(excelPath);
		sheet = workbook.getSheet(sheetName);
		row = sheet.getRow(rowNum);
		cell = row.createCell(colNum);
		cell.setCellValue(data);
		
		}
	
	}
