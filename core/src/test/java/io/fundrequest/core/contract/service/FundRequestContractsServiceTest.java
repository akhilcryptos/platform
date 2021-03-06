package io.fundrequest.core.contract.service;

import io.fundrequest.common.infrastructure.exception.ResourceNotFoundException;
import io.fundrequest.core.contract.domain.FundRequestContract;
import io.fundrequest.core.contract.domain.TokenWhitelistPreconditionContract;
import io.fundrequest.core.token.TokenInfoService;
import io.fundrequest.core.token.dto.TokenInfoDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.web3j.protocol.Web3j;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FundRequestContractsServiceTest {

    private FundRequestContractsService fundRequestContractsService;
    private TokenWhitelistPreconditionContract tokenWhitelistPreconditionContract;
    private TokenInfoService tokenInfoService;

    @BeforeEach
    void setUp() {
        tokenWhitelistPreconditionContract = mock(TokenWhitelistPreconditionContract.class, RETURNS_DEEP_STUBS);
        tokenInfoService = mock(TokenInfoService.class);
        fundRequestContractsService = new FundRequestContractsService(
                mock(FundRequestContract.class),
                tokenWhitelistPreconditionContract,
                mock(Web3j.class),
                tokenInfoService
        );
    }

    @Test
    void testNoDuplicates() throws Exception {
        String platform = "GITHUB";
        String platformId = "FundRequest|FR|area51|FR|3";

        when(tokenWhitelistPreconditionContract.amountOftokens().send().intValue()).thenReturn(3);
        when(tokenWhitelistPreconditionContract.token(BigInteger.valueOf(0))).thenReturn(Optional.of("FND"));
        when(tokenWhitelistPreconditionContract.token(BigInteger.valueOf(1))).thenReturn(Optional.of("ZRX"));
        when(tokenWhitelistPreconditionContract.token(BigInteger.valueOf(2))).thenReturn(Optional.of("ZRX"));
        when(tokenInfoService.getTokenInfo("FND")).thenReturn(TokenInfoDto.builder().symbol("FND").address("FND").build());
        when(tokenInfoService.getTokenInfo("ZRX")).thenReturn(TokenInfoDto.builder().symbol("ZRX").address("ZRX").build());
        when(tokenWhitelistPreconditionContract.isValid(platform, platformId, "FND")).thenReturn(true);
        when(tokenWhitelistPreconditionContract.isValid(platform, platformId, "ZRX")).thenReturn(true);


        List<TokenInfoDto> possibleTokens = fundRequestContractsService.getAllPossibleTokens(platform, platformId);

        assertThat(possibleTokens.stream().map(TokenInfoDto::getSymbol).collect(Collectors.toList())).containsExactly("FND", "ZRX");
    }

    @Test
    public void getAllPossibleTokens_throwsResourceNotFoundException_whenEmpty() {
        final String platform = "GITHUB";
        final String platformId = "FundRequest|FR|area51|FR|3";

        when(tokenWhitelistPreconditionContract.token(any(BigInteger.class))).thenReturn(Optional.empty());

        try {
            fundRequestContractsService.getAllPossibleTokens(platform, platformId);
            fail("A ResourceNotFoundException should have been thrown");
        } catch (ResourceNotFoundException e) {
            assertThat(e).isNotNull();
        }
    }
}