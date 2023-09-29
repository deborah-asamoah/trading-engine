package io.turntabl.project.clientprocessingapi.dtos.responsebodies;

import io.turntabl.project.clientprocessingapi.dtos.PortfolioDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
public class ListPortfoliosResponseBody {
    List<PortfolioDTO> portfolioDTOS;
}
