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

package com.dartshark.data;

import java.util.Calendar;

import javax.persistence.Entity;
import javax.persistence.Id;


@Entity
public class ActiveWebSocketPlayer {
	@Id
	private String id;

	private String username;

	private Calendar connectionTime;
	
	private String first_name;
	
	private String last_name;

	public ActiveWebSocketPlayer() {
	}

	public ActiveWebSocketPlayer(String id, String username, Calendar connectionTime, String first_name, String last_name) {
		super();
		this.id = id;
		this.username = username;
		this.connectionTime = connectionTime;
		this.first_name = first_name;
		this.last_name = last_name;
	}

	public String getFirst_name() {
		return first_name;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public String getLast_name() {
		return last_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Calendar getConnectionTime() {
		return this.connectionTime;
	}

	public void setConnectionTime(Calendar connectionTime) {
		this.connectionTime = connectionTime;
	}

}
