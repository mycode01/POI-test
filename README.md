# Apache POI test

apache재단의 poi라는 라이브러리를 이용하여    
ms word, excel을 핸들링 할수있음을 테스트 하였음.    
word(docx)의 경우 순차적으로 iterating 을 시켜가며 파싱할수있으며, 
표는 \t와 \n로 구성되어 있으며 문제없이 map형태로 파싱은 가능하나
형태가 복잡해진다면 꼬일 위험이 있을수있음.    
예상하다시피 word포멧 자체가 너무 자유분방한 포멧이라 적당히 포멧을 강제하지 않으면 파싱이 매우 더럽고 어려워질듯함.    
doc파일도 순차적으로 파싱 가능함.    
다만 개행문자가 \u0007 로 추가되어 따라다녀 처리가 필요함.    


XSSF > xlsx
HSSF > xls
xssf의 경우 workbook > sheet > row > cell순으로 불러와야함.    
좌표를 안다고 해서 ``sheet.getCell(x,y)`` 식으로 부를수 없고,    
``sheet.getRow(y).getCell(x)``로 불러야함.    
평문이 아닌 table의 경우(삽입 > 테이블 로 만들어진) 객체화 시킬수 있지만    
마찬가지로 row.cell로 찾아가야함.    
``row.getCell(j, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);``
처럼 null처리 필요함.
