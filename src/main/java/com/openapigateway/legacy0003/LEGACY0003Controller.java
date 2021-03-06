package com.openapigateway.legacy0003;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class LEGACY0003Controller {

    @Autowired
    private LEGACY0003Service legacy0003Service;

    @RequestMapping(value = "/LEGACY0003/", method = GET)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity getCredits() {
        log.debug("getCredits {}", "start");

        String credits = legacy0003Service.getCredits();

        return new ResponseEntity<>(credits, HttpStatus.OK);
    }

    @RequestMapping(value = "/LEGACY0003/{userId}", method = GET)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity getCreditByPk(@PathVariable String userId) {
        log.debug("getCreditByPk {} {}", "start", userId);

        String credits = legacy0003Service.getCreditByPk(userId);

        return new ResponseEntity<>(credits, HttpStatus.OK);
    }

}
