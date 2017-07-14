package io.seldon.engine;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.google.protobuf.InvalidProtocolBufferException;

import io.seldon.engine.exception.APIException;
import io.seldon.engine.pb.ProtoBufUtils;
import io.seldon.protos.PredictionProtos.PredictionStatusDef;


@ControllerAdvice
public class ExceptionControllerAdvice {

	@ExceptionHandler(APIException.class)
	public ResponseEntity<String> handleUnauthorizedException(APIException exception) throws InvalidProtocolBufferException {

		
		PredictionStatusDef.Builder statusBuilder = PredictionStatusDef.newBuilder();
		statusBuilder.setCode(exception.getApiExceptionType().getId());
		statusBuilder.setReason(exception.getApiExceptionType().getMessage());
		statusBuilder.setInfo(exception.getInfo());
		statusBuilder.setStatus(PredictionStatusDef.Status.FAILURE);		
		
		PredictionStatusDef status = statusBuilder.build();
		String json;
		json = ProtoBufUtils.toJson(status);
		return new ResponseEntity<String>(json,HttpStatus.valueOf(exception.getApiExceptionType().getHttpCode())); 


	}

}