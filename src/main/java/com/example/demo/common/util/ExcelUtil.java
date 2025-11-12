package com.example.demo.common.util;

import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ExcelUtil {

  public static void downloadXlsx(HttpServletResponse response,
                                  String fileName,
                                  List<String> headers,
                                  List<List<String>> rows) {
    // 1) 응답 헤더 세팅
    try {
      String encoded = "attachment; filename*=UTF-8''" +
        URLEncoder.encode(fileName, StandardCharsets.UTF_8).replaceAll("\\+", "%20");
      response.setContentType(
        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
      response.setHeader("Content-Disposition", encoded);
      response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
      response.setHeader("Pragma", "no-cache");
      response.setHeader("Expires", "0");

      // 2) 워크북/시트
      try (Workbook wb = new XSSFWorkbook(); OutputStream os = response.getOutputStream()) {
        Sheet sheet = wb.createSheet("data");

        // 3) 스타일: 헤더 굵게 + 회색 배경
        CellStyle headerStyle = wb.createCellStyle();
        Font bold = wb.createFont();
        bold.setBold(true);
        headerStyle.setFont(bold);
        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setBorderTop(BorderStyle.THIN);
        headerStyle.setBorderBottom(BorderStyle.THIN);
        headerStyle.setBorderLeft(BorderStyle.THIN);
        headerStyle.setBorderRight(BorderStyle.THIN);

        CellStyle bodyStyle = wb.createCellStyle();
        bodyStyle.setBorderTop(BorderStyle.THIN);
        bodyStyle.setBorderBottom(BorderStyle.THIN);
        bodyStyle.setBorderLeft(BorderStyle.THIN);
        bodyStyle.setBorderRight(BorderStyle.THIN);

        int rowIdx = 0;

        // 4) 헤더
        if (headers != null && !headers.isEmpty()) {
          Row headerRow = sheet.createRow(rowIdx++);
          for (int c = 0; c < headers.size(); c++) {
            Cell cell = headerRow.createCell(c, CellType.STRING);
            cell.setCellValue(sanitize(headers.get(c)));
            cell.setCellStyle(headerStyle);
          }
          // 필터 & 첫 행 고정
          sheet.setAutoFilter(new org.apache.poi.ss.util.CellRangeAddress(
            0, 0, 0, headers.size() - 1));
          sheet.createFreezePane(0, 1);
        }

        // 5) 바디
        if (rows != null) {
          for (List<String> r : rows) {
            Row row = sheet.createRow(rowIdx++);
            int colCount = (headers == null || headers.isEmpty())
              ? (r == null ? 0 : r.size())
              : headers.size();
            for (int c = 0; c < colCount; c++) {
              String val = (r != null && c < r.size()) ? r.get(c) : "";
              Cell cell = row.createCell(c, CellType.STRING);
              cell.setCellValue(sanitize(val));
              cell.setCellStyle(bodyStyle);
            }
          }
        }

        // 6) 오토사이즈 (열이 많으면 비용이 크니 최대 50열까지만)
        int maxCols = (headers == null) ? 0 : headers.size();
        int limit = Math.min(maxCols, 50);
        for (int c = 0; c < limit; c++) {
          sheet.autoSizeColumn(c);
          // 너무 좁게 나오는 경우를 막기 위한 최소폭(옵션)
          int width = sheet.getColumnWidth(c);
          sheet.setColumnWidth(c, Math.max(width, 3000)); // 약 12px
        }

        // 7) 실제 전송
        wb.write(os);
        os.flush();
      }
    } catch (Exception e) {
      // 필요 시 로깅 후 적절한 에러 처리
      throw new RuntimeException("엑셀 생성/전송 중 오류", e);
    }
  }

  /**
   * 엑셀/CSV 포뮬러 인젝션 방지: =, +, -, @ 로 시작하면 앞에 ' 를 붙인다.
   * null -> "" (빈 문자열)
   */
  private static String sanitize(String s) {
    if (s == null) return "";
    String trimmed = s.trim();
    if (trimmed.isEmpty()) return "";
    char ch = trimmed.charAt(0);
    if (ch == '=' || ch == '+' || ch == '-' || ch == '@') {
      return "'" + trimmed;
    }
    return trimmed;
  }

}
