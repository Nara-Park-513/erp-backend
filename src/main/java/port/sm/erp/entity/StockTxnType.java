package port.sm.erp.entity;

//1) 오타방지, 2) 가독성, 3)DB 값의 표준화, 4)비즈니스 로직으로 분리, 5) 기능 추가 쉬워짐
public enum StockTxnType {
    IN, OUT, ADJUST, RESERVE, RELEASE
}
//데이터 타입을 정해진 선택지로 잠궈서 실수 못 하게 하는 안전 장치
