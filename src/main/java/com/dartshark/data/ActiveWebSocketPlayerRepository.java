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

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ActiveWebSocketPlayerRepository
		extends JpaRepository<ActiveWebSocketPlayer, String> {

//	@Query("select DISTINCT(u.username) from ActiveWebSocketPlayer u")
//	List<String> findAllActiveUsers();
	
	@Query("SELECT u FROM ActiveWebSocketPlayer u")
	Collection<ActiveWebSocketPlayer> findAllActiveUsers();
}

