package io.turntabl.project.clientservice.mappers;


import io.turntabl.project.clientprocessingapi.dtos.PortfolioDTO;
import io.turntabl.project.clientprocessingapi.dtos.requestbodies.CreatePortfolioRequestBody;
import io.turntabl.project.persistence.entities.Portfolio;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PortfolioRequestBodyMapper {
    Portfolio toPortfolio(CreatePortfolioRequestBody createPortfolioRequestBody);

    PortfolioDTO toPortfolioDTO(Portfolio portfolio);
}
