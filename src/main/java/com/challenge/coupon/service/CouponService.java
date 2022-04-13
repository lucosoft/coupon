package com.challenge.coupon.service;

import com.challenge.coupon.dto.Item;
import com.challenge.coupon.dto.ItemAcumulator;
import com.challenge.coupon.dto.ItemsRequest;
import com.challenge.coupon.dto.ItemsResponse;
import com.challenge.coupon.exception.CouponException;
import com.challenge.coupon.model.Favorites;
import com.challenge.coupon.repository.FavoritesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.net.Proxy;
import java.text.DecimalFormat;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static com.challenge.coupon.constants.Constants.*;

@Service
public class CouponService {

    private final static Logger logger = Logger.getLogger("com.challenge.coupon.service.CouponService");
    private static final DecimalFormat df = new DecimalFormat("0.00");

    @Value("${item.price.url}")
    private String itemPriceUrl;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    FavoritesRepository favoritesRepository;

    @Autowired
    public CouponService(FavoritesRepository favoritesRepository){
        this.favoritesRepository = favoritesRepository;
    }


    public ItemsResponse purchasingCapacity(ItemsRequest itemsRequest){

        this.validateItemsRequest(itemsRequest);

        ItemAcumulator itemAcumulator = new ItemAcumulator(itemsRequest.getAmount());
        itemsRequest.getItemIds().stream().distinct().forEach((i) -> itemAcumulator.add(getItemPrice(i)));

        ItemsResponse response = new ItemsResponse();
        response.setItemIds(itemAcumulator.getItems().stream()
                                .map(Item::getId).collect(Collectors.toList()));
        response.setTotal(itemAcumulator.getAmount());

        this.updateFavorites(itemsRequest.getItemIds());

        return response;
    }

    private void validateItemsRequest(ItemsRequest itemsRequest){
        if(itemsRequest.getItemIds().isEmpty()){
            throw new CouponException(ERR_REQUEST_EMPTY_ID_CODE, ERR_REQUEST_EMPTY_ID_DESC, HttpStatus.BAD_REQUEST);
        }

        if(!itemsRequest.getItemIds().stream()
                .allMatch(ItemsRequest::validate)) {
            throw new CouponException(ERR_REQUEST_BAD_ID_CODE, ERR_REQUEST_BAD_ID_DESC, HttpStatus.BAD_REQUEST);

        }
    }

    private void updateFavorites(List<String> itemIds) {

        itemIds.stream().distinct().forEach(id -> favoritesRepository.updateFavoritesById(id));
    }

    public List<Favorites> getFavorites() {

        List<Favorites> favorites = favoritesRepository.getTopFavorites();
        logger.info(() -> "favorites: " + favorites);
        return favorites;
    }

     private Item getItemPrice(String itemId) throws CouponException {

         logger.info(() -> "Trying to get info from: " + itemPriceUrl);

         SimpleClientHttpRequestFactory clientHttpReq = new SimpleClientHttpRequestFactory();
         clientHttpReq.setProxy(Proxy.NO_PROXY);
         clientHttpReq.setConnectTimeout(1 * 1000);
         clientHttpReq.setReadTimeout(3 * 1000);
         restTemplate.setRequestFactory(clientHttpReq);

         ResponseEntity<Item> response;
         Item item;
         try {
             HttpHeaders httpHeaders = new HttpHeaders();
             httpHeaders.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
             httpHeaders.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

             response = restTemplate.getForEntity(itemPriceUrl.concat(itemId), Item.class);
             item = response.getBody();

         } catch(HttpClientErrorException | HttpServerErrorException e) {
             logger.severe(() -> ERR_COUPON_API_ITEM_DESC);
             throw new CouponException(ERR_COUPON_API_ITEM_CODE, ERR_COUPON_API_ITEM_DESC, e.getMessage(), e.getStatusCode());
         } catch(ResourceAccessException e) {
             logger.severe(() -> ERR_COUPON_API_ITEM_DESC);
             throw new CouponException(ERR_COUPON_API_ITEM_ACCESS_CODE, ERR_COUPON_API_ITEM_ACCESS_DESC, e.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
         }
         logger.info(() -> "Get Item Price Succesfull from " + item.getId());
         return item;
     }
}
