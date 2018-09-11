/*
 * Copyright 2014-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dartshark.websocket;

import java.security.Principal;
import java.util.Arrays;
import java.util.Calendar;

import org.springframework.context.ApplicationListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.socket.messaging.SessionConnectEvent;

import com.dartshark.data.ActiveWebSocketPlayer;
import com.dartshark.data.ActiveWebSocketPlayerRepository;
import com.dartshark.data.Player;
import com.dartshark.data.PlayerRepository;

public class WebSocketConnectHandler<S>
		implements ApplicationListener<SessionConnectEvent> {
	private ActiveWebSocketPlayerRepository repository;
	private SimpMessageSendingOperations messagingTemplate;
	private PlayerRepository playerRepo;

	public WebSocketConnectHandler(SimpMessageSendingOperations messagingTemplate,
			ActiveWebSocketPlayerRepository repository, PlayerRepository playerRepo) {
		super();
		this.messagingTemplate = messagingTemplate;
		this.repository = repository;
		this.playerRepo = playerRepo;
	}

	@Override
	public void onApplicationEvent(SessionConnectEvent event) {
		MessageHeaders headers = event.getMessage().getHeaders();
		Principal user = SimpMessageHeaderAccessor.getUser(headers);
		if (user == null) {
			return;
		}
		String id = SimpMessageHeaderAccessor.getSessionId(headers);
		Player current = playerRepo.findByEmail(user.getName());
		this.repository.save(
				new ActiveWebSocketPlayer(id, user.getName(), Calendar.getInstance(), current.getFirstName(), current.getLastName()));
		this.messagingTemplate.convertAndSend("/topic/friends/signin",
				current);
	}
}
