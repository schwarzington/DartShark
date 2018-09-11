/*
 * Copyright 2014-2018 the original author or authors.
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

import java.util.Arrays;

import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import com.dartshark.data.ActiveWebSocketPlayerRepository;
import com.dartshark.data.Player;
import com.dartshark.data.PlayerRepository;

public class WebSocketDisconnectHandler<S>
		implements ApplicationListener<SessionDisconnectEvent> {
	private ActiveWebSocketPlayerRepository repository;
	private SimpMessageSendingOperations messagingTemplate;
	private PlayerRepository playerRepo;

	public WebSocketDisconnectHandler(SimpMessageSendingOperations messagingTemplate,
			ActiveWebSocketPlayerRepository repository, PlayerRepository playerRepo) {
		super();
		this.messagingTemplate = messagingTemplate;
		this.repository = repository;
		this.playerRepo = playerRepo;
	}

	@Override
	public void onApplicationEvent(SessionDisconnectEvent event) {
		String id = event.getSessionId();
		if (id == null) {
			return;
		}
		this.repository.findById(id).ifPresent((user) -> {
			Player current = playerRepo.findByEmail(user.getUsername());
			this.repository.deleteById(id);
			this.messagingTemplate.convertAndSend("/topic/friends/signout",
					current);
		});
	}
}
