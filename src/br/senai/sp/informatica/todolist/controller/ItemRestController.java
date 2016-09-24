package br.senai.sp.informatica.todolist.controller;

import java.net.URI;

import org.json.HTTP;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.senai.sp.informatica.todolist.DAO.ItemDao;
import br.senai.sp.informatica.todolist.modelo.ItemLista;
import br.senai.sp.informatica.todolist.modelo.Lista;

@RestController
public class ItemRestController {

	@Autowired
	private ItemDao itemDao;

	@RequestMapping(value = "/item/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Void> mudarFeito(@PathVariable("id") long idItem, @RequestBody String feito) {
		try {
			JSONObject job = new JSONObject(feito);
			itemDao.marcarFeito(idItem, job.getBoolean("feito"));

			HttpHeaders responseHeader = new HttpHeaders();
			URI location = new URI("/item/" + idItem);
			responseHeader.setLocation(location);
			return new ResponseEntity<Void>(responseHeader, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@RequestMapping(value = "/lista/{idLista}/item", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<ItemLista> inserir(@PathVariable long idLista, @RequestBody ItemLista item) {
		try {
			itemDao.inserir(idLista, item);
			URI location = new URI("/item/" + item.getId());
			return ResponseEntity.created(location).body(item);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	@RequestMapping(value="/item/{idItem}",method=RequestMethod.GET
			,produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ItemLista consultar(@PathVariable long idItem){
		return itemDao.consultar(idItem);
	}

}
