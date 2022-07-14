package com.example.demo.service;

import com.ibm.icu.util.ChineseCalendar;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class DemoService {
    KoreanLunarCalendar calendar = KoreanLunarCalendar.getInstance();
    Integer year;
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");

    // 전체 공휴일 리스트를 받음
    public List getHolliday() {
        this.year = LocalDateTime.now().getYear();

        ArrayList<String> holidayList = new ArrayList<>();

        // 법정 지정 공휴일
        // 양력 공휴일
        String[] SolarHoliday = {
                convertDate2String(LocalDateTime.of(year, 1, 1, 0, 0, 0)), // 신정

                convertDate2String(LocalDateTime.of(year, 3, 1, 0, 0, 0)), // 3.1절
                substituteHoliday(LocalDateTime.of(year, 3, 1, 0, 0, 0), "3.1절"),  // 3.1절 대체공휴일

                convertDate2String(LocalDateTime.of(year, 5, 5, 0, 0, 0)), // 어린이날
                substituteHoliday(LocalDateTime.of(year, 5, 5, 0, 0, 0), "어린이날"),  // 어린이날 대체공휴일

                convertDate2String(LocalDateTime.of(year, 6, 6, 0, 0, 0)),  // 현충일

                convertDate2String(LocalDateTime.of(year, 8, 15, 0, 0, 0)),  // 광복절
                substituteHoliday(LocalDateTime.of(year, 8, 15, 0, 0, 0), "광복절"),  // 광복절 대체공휴일

                convertDate2String(LocalDateTime.of(year, 10, 3, 0, 0, 0)),  // 개천절
                substituteHoliday(LocalDateTime.of(year, 10, 3, 0, 0, 0), "개천절"),  // 개천절 대체공휴일

                convertDate2String(LocalDateTime.of(year, 10, 9, 0, 0, 0)),  // 한글날
                substituteHoliday(LocalDateTime.of(year, 10, 9, 0, 0, 0), "한글날"),  // 한글날 대체공휴일

                convertDate2String(LocalDateTime.of(year, 12, 25, 0, 0, 0)),   // 크리스마스
        };
        ArrayList<String> SolarList = new ArrayList<String>(Arrays.asList(SolarHoliday));

        // 음력 공휴일
        String[] LunarHoliday = {
                convertDate2String(convertLunar2Solar(LocalDateTime.of(year, 1, 1, 0, 0, 0)).minusDays(1)), // 설날 연휴
                convertDate2String(convertLunar2Solar(LocalDateTime.of(year, 1, 1, 0, 0, 0))),  // 설날
                convertDate2String(convertLunar2Solar(LocalDateTime.of(year, 1, 1, 0, 0, 0)).plusDays(1)),  // 설날 연휴
                substituteHoliday(convertLunar2Solar(LocalDateTime.of(year, 1, 1, 0, 0, 0)), "설날"),

                convertDate2String(convertLunar2Solar(LocalDateTime.of(year, 8, 15, 0, 0, 0)).minusDays(1)), // 추석 연휴
                convertDate2String(convertLunar2Solar(LocalDateTime.of(year, 8, 15, 0, 0, 0))), // 추석
                convertDate2String(convertLunar2Solar(LocalDateTime.of(year, 8, 15, 0, 0, 0)).plusDays(1)),  // 추석 연휴
                substituteHoliday(convertLunar2Solar(LocalDateTime.of(year, 8, 15, 0, 0, 0)), "추석")
        };
        ArrayList<String> LunarList = new ArrayList<String>(Arrays.asList(LunarHoliday));

        calendar.setLunarDate(year, 4, 8, false);  // 석가탄신일
        holidayList.add(calendar.getSolarIsoFormat());

        holidayList.addAll(SolarList);
        holidayList.addAll(LunarList);

        Collections.sort(holidayList); // 날짜순으로 정렬
        holidayList.removeAll(Arrays.asList(""));

        return holidayList;
    }

    // 음력 > 양력 변환
    public LocalDateTime convertLunar2Solar(LocalDateTime lunarDate) {
        ChineseCalendar lunarCalendar = new ChineseCalendar();

        if (lunarDate.equals(null)) return null;

        int lunar_year = lunarDate.getYear(),
                lunar_month = lunarDate.getMonthValue(),
                lunar_day = lunarDate.getDayOfMonth();

        lunarCalendar.set(ChineseCalendar.EXTENDED_YEAR, lunar_year + 2637);
        lunarCalendar.set(ChineseCalendar.MONTH, lunar_month - 1);
        lunarCalendar.set(ChineseCalendar.DAY_OF_MONTH, lunar_day);

        LocalDateTime solarDate = Instant.ofEpochMilli(lunarCalendar.getTimeInMillis()).atZone(ZoneId.of("UTC")).toLocalDateTime();

        return solarDate;
    }

    /*
     * 대체공휴일 적용
     *   설날, 어린이날, 추석
     *   광복절, 개천절, 한글날
     *   3.1절
     */
    public String substituteHoliday(LocalDateTime date, String holidayName) {
        String returnDate = new String();
        int getDate = date.getDayOfWeek().getValue();

        switch (holidayName) {
            case "설날":
            case "추석":
                if (getDate == 6 || getDate == 7) returnDate = convertDate2String(date.plusDays(2));
                break;
            default:
                if (getDate == 6) returnDate = convertDate2String(date.plusDays(2));
                if (getDate == 7) returnDate = convertDate2String(date.plusDays(1));
                break;
        }

        return returnDate;
    }

    public String convertDate2String(LocalDateTime date) {
        return date.format(dateTimeFormatter);
    }

    public LocalDateTime getFinishDate(LocalDateTime date, Integer count) {
        Integer plusDays = 0;

        List<String> holidayList = this.getHolliday();
        List<Integer> WEEKDAYS = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4, 5));

        while (count > 1) {
            plusDays++;

            LocalDateTime tempDate = date.plusDays(plusDays);
            Integer now = tempDate.getDayOfWeek().getValue();
            // 평일 or 주말
            if (WEEKDAYS.contains(now)) {
                // 평일이 공휴일인가?
                if (!holidayList.contains(convertDate2String(tempDate))) {
                    count--;
                } else {
                    // 공휴일이면 SKIP
                }
            } else {
                // 주말이면 무조건 SKIP
            }
        };

        return date.plusDays(plusDays);
    }
}