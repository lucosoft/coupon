package com.challenge.coupon.controller;

import com.challenge.coupon.dto.ErrorResponse;
import com.challenge.coupon.dto.ItemsRequest;
import com.challenge.coupon.dto.ItemsResponse;
import com.challenge.coupon.exception.CouponException;
import com.challenge.coupon.model.Favorites;
import com.challenge.coupon.service.CouponService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.challenge.coupon.constants.Constants.ERR_COUPON_NOT_VALID_REQUEST_ERROR_CODE;
import static com.challenge.coupon.constants.Constants.ERR_COUPON_NOT_VALID_REQUEST_ERROR_DESC;

@ControllerAdvice
@RestController
@RequestMapping(value = "/coupon")
public class CouponController {

    @Autowired
    CouponService service;

    @ApiOperation(value = "Recibe una lista de item_ids y el monto del cupón y devuelve los items que tendría que comprar el usuario",
            response = ItemsResponse.class,
            produces = "application/json")
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity purchasingCapacity(@Valid @RequestBody ItemsRequest itemsRequest, BindingResult errors) {
        if (errors.hasErrors())
            throw new CouponException(ERR_COUPON_NOT_VALID_REQUEST_ERROR_CODE, ERR_COUPON_NOT_VALID_REQUEST_ERROR_DESC, errors.getAllErrors().get(0).getDefaultMessage().toString(), HttpStatus.BAD_REQUEST);

        ItemsResponse response = service.purchasingCapacity(itemsRequest);
        return new ResponseEntity<Object>(response, HttpStatus.OK);
    }

    @ApiOperation(value = "Devuelve el top 5 de ítems más favoriteados",
            response = Favorites.class,
            produces = "application/json")
    @RequestMapping(method = RequestMethod.GET, value = "/stats")
    public ResponseEntity getFavorites() {
        List<Favorites> response = service.getFavorites();
        return new ResponseEntity<Object>(response, HttpStatus.OK);
    }

    @ExceptionHandler(CouponException.class)
    public ResponseEntity<ErrorResponse> handleExceptions(Exception e) {

        CouponException couponException = (CouponException) e;

        return new ResponseEntity<>(new ErrorResponse(couponException),
                                    couponException.getStatus());
    }
}
