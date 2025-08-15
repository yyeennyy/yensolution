package util.date;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;

public class DateUtil {
    
    private static final DateTimeFormatter DEFAULT_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    // =========================== 현재 날짜/시간 조회 ===========================
    
    /**
     * 현재 날짜를 문자열로 반환
     */
    public static String getCurrentDateString() {
        return LocalDate.now().format(DEFAULT_FORMATTER);
    }
    
    /**
     * 현재 날짜시간을 문자열로 반환
     */
    public static String getCurrentDateTimeString() {
        return LocalDateTime.now().format(DATETIME_FORMATTER);
    }
    
    /**
     * 현재 날짜/시간을 지정한 패턴의 문자열로 반환
     */
    public static String now(String pattern) {
        return format(LocalDateTime.now(), pattern);
    }
    
    // =========================== 포맷 변환 ===========================
    
    /**
     * LocalDate를 지정한 패턴의 문자열로 변환
     */
    public static String format(LocalDate date, String pattern) {
        if (date == null || pattern == null) return null;
        return date.format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * LocalDateTime을 지정한 패턴의 문자열로 변환
     */
    public static String format(LocalDateTime dateTime, String pattern) {
        if (dateTime == null || pattern == null) return null;
        return dateTime.format(DateTimeFormatter.ofPattern(pattern));
    }
    
    // =========================== 파싱 ===========================
    
    /**
     * 문자열을 LocalDate로 파싱
     */
    public static LocalDate parseToDate(String dateStr, String pattern) {
        try {
            return LocalDate.parse(dateStr, DateTimeFormatter.ofPattern(pattern));
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    /**
     * 문자열을 LocalDateTime으로 파싱
     */
    public static LocalDateTime parseToDateTime(String dateTimeStr, String pattern) {
        try {
            return LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ofPattern(pattern));
        } catch (DateTimeParseException e) {
            return null;
        }
    }
    
    // =========================== 날짜 계산 ===========================
    
    /**
     * 두 날짜 사이의 일수 계산
     */
    public static long getDaysBetween(LocalDate startDate, LocalDate endDate) {
        return ChronoUnit.DAYS.between(startDate, endDate);
    }
    
    /**
     * 나이 계산
     */
    public static int calculateAge(LocalDate birthDate) {
        return Period.between(birthDate, LocalDate.now()).getYears();
    }
    
    // =========================== 날짜 조정 ===========================
    
    /**
     * 해당 월의 첫 번째 날
     */
    public static LocalDate getFirstDayOfMonth(LocalDate date) {
        return date.with(TemporalAdjusters.firstDayOfMonth());
    }
    
    /**
     * 해당 월의 마지막 날
     */
    public static LocalDate getLastDayOfMonth(LocalDate date) {
        return date.with(TemporalAdjusters.lastDayOfMonth());
    }
    
    /**
     * 다음 월요일 찾기
     */
    public static LocalDate getNextMonday(LocalDate date) {
        return date.with(TemporalAdjusters.next(DayOfWeek.MONDAY));
    }
    
    // =========================== 업무일 관련 ===========================
    
    /**
     * 평일인지 확인
     */
    public static boolean isWeekday(LocalDate date) {
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        return dayOfWeek != DayOfWeek.SATURDAY && dayOfWeek != DayOfWeek.SUNDAY;
    }
    
    /**
     * 업무일 추가 (주말 제외)
     */
    public static LocalDate addBusinessDays(LocalDate date, int days) {
        LocalDate result = date;
        int addedDays = 0;
        
        while (addedDays < days) {
            result = result.plusDays(1);
            if (isWeekday(result)) {
                addedDays++;
            }
        }
        
        return result;
    }
    
    // =========================== 타임스탬프 ===========================
    
    /**
     * 타임스탬프 생성
     */
    public static long getCurrentTimestamp() {
        return Instant.now().getEpochSecond();
    }
    
    /**
     * 타임스탬프를 날짜로 변환
     */
    public static LocalDateTime timestampToLocalDateTime(long timestamp) {
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp), ZoneId.systemDefault());
    }
}