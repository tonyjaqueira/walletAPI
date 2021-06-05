package com.wallet.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wallet.dto.WallateItemDTO;
import com.wallet.entity.Wallet;
import com.wallet.entity.WalletItem;
import com.wallet.response.Response;
import com.wallet.service.WalletItemService;
import com.wallet.util.enums.TypeEnum;

@RestController
@RequestMapping("wallet-item")
public class WalletItemController {

	@Autowired
	private WalletItemService service;
	
	@PostMapping
	public ResponseEntity<Response<WallateItemDTO>> create(@Valid @RequestBody WallateItemDTO dto, BindingResult result){
		Response<WallateItemDTO> response = new Response<WallateItemDTO>();
		if (result.hasErrors()) {
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}
		WalletItem wi = service.save(this.convertDtoToEntity(dto));
		response.setData(this.convertEntityToDto(wi));
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
	
	@GetMapping(value="/{wallet}")
	public ResponseEntity<Response<Page<WallateItemDTO>>> findBetweenDates(@PathVariable("wallet") Long wallet,
			@RequestParam("starDate") @DateTimeFormat(pattern = "dd-MM-yyyy") Date starDate,
			@RequestParam("endDate") @DateTimeFormat(pattern = "dd-MM-yyyy") Date endDate,
			@RequestParam(name="page", defaultValue = "0") int page) {
		
		Response<Page<WallateItemDTO>> response = new Response<Page<WallateItemDTO>>();
		Page<WalletItem> items = service.findBetweenDates(wallet, starDate, endDate, page);
		Page<WallateItemDTO> dto = items.map(i -> this.convertEntityToDto(i));
		response.setData(dto);
		return ResponseEntity.ok().body(response);
	}
	
	@GetMapping(value="/type/{wallet}")
	public ResponseEntity<Response<List<WallateItemDTO>>> findByWalletIdANdType(@PathVariable("wallet") Long wallet,
			@RequestParam("type") String type) {
		
		Response<List<WallateItemDTO>> response = new Response<List<WallateItemDTO>>();
		List<WalletItem> list = service.findByWalletAndType(wallet, TypeEnum.getEnum(type));
		
		List<WallateItemDTO> dto = new ArrayList<>();
		list.forEach(i -> dto.add(this.convertEntityToDto(i)));
		response.setData(dto);
		return ResponseEntity.ok().body(response);
	}
	
	@GetMapping(value="/total/{wallet}")
	public ResponseEntity<Response<BigDecimal>> sumByWalletId(@PathVariable("wallet") Long wallet) {
		
		Response<BigDecimal> response = new Response<BigDecimal>();
		BigDecimal value = service.sumByWalletId(wallet);
		response.setData(value == null ? BigDecimal.ZERO : value);
		return ResponseEntity.ok().body(response);
	}
	
	@PutMapping
	public ResponseEntity<Response<WallateItemDTO>> update(@Valid @RequestBody WallateItemDTO dto, BindingResult result){
		Response<WallateItemDTO> response = new Response<WallateItemDTO>();
		Optional<WalletItem> wi = service.findById(dto.getId());
		
		if(!wi.isPresent()) {
			result.addError(new ObjectError("WalletItem", "WalletItem não encontrado"));
		} else if(wi.get().getWallet().getId().compareTo(dto.getWallet()) != 0) { //diferente de zewro quer dizer q os ids são diferentes
			result.addError(new ObjectError("WalletItem", "Você não pode alterar a carteira"));
		}
		
		if (result.hasErrors()) {
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}
		
		WalletItem saved = service.save(this.convertDtoToEntity(dto));
		
		response.setData(this.convertEntityToDto(saved));
		return ResponseEntity.ok().body(response);
	}
	
	@DeleteMapping(value="/delete/{walletItemId}")
	public ResponseEntity<Response<String>> findByWalletIdAndType(@PathVariable("walletItemId") Long walletItemId) {
		Response<String> response = new Response<String>();
		Optional<WalletItem> wi = service.findById(walletItemId);
		
		if(!wi.isPresent()) {
			response.getErrors().add("Carteira de id "+walletItemId+ " não encontrada");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}
		
		service.deleteById(walletItemId);
		response.setData("Carteira de id "+walletItemId+ " apagada com sucesso");
		return ResponseEntity.ok().body(response);
	}
	
	
	
	
	private WalletItem convertDtoToEntity(WallateItemDTO dto) {
		WalletItem wi = new WalletItem();
		wi.setId(dto.getId());
		wi.setDate(dto.getDate());
		wi.setDescription(dto.getDescription());
		wi.setType(TypeEnum.getEnum(dto.getType()));
		wi.setValue(dto.getValue());
		
		Wallet w = new Wallet();
		w.setId(dto.getWallet());
		
		wi.setWallet(w);
		return wi;
	}
	
	private WallateItemDTO convertEntityToDto(WalletItem wi) {
		WallateItemDTO dto = new WallateItemDTO();
		dto.setId(wi.getId());
		dto.setDate(wi.getDate());
		dto.setDescription(wi.getDescription());
		dto.setType(wi.getType().getValue());
		dto.setValue(wi.getValue());
		dto.setWallet(wi.getWallet().getId());
		return dto;
	}
	
}
