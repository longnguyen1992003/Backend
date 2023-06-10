package net.javaguides.mssql.EmployeeDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ListEmployeeResponeDto {
    private List<ListEmployeeDto> listEmployeeDtoList;
    private long pageSize;
    private int pageNo;
    private String totalElements;
    private String totalPages;
    private boolean last;

}
