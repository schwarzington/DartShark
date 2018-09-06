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

package com.dartshark.security;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.dartshark.data.Player;
import com.dartshark.data.PlayerRepository;

/**
 * @author Rob Winch
 *
 */
@Service
public class PlayerRepositoryUserDetailsService implements UserDetailsService {
	private final PlayerRepository playerRepository;

	@Autowired
	public PlayerRepositoryUserDetailsService(PlayerRepository playerRepository) {
		this.playerRepository = playerRepository;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.springframework.security.core.userdetails.UserDetailsService#loadUserByUsername
	 * (java.lang.String)
	 */
	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		Player Player = this.playerRepository.findByEmail(username);
		if (Player == null) {
			throw new UsernameNotFoundException("Could not find Player " + username);
		}
		return new CustomPlayerDetails(Player);
	}

	private static final class CustomPlayerDetails extends Player implements UserDetails {

		private CustomPlayerDetails(Player player) {
			super(player);
		}

		@Override
		public Collection<? extends GrantedAuthority> getAuthorities() {
			return AuthorityUtils.createAuthorityList("ROLE_USER");
		}

		@Override
		public String getUsername() {
			return getEmail();
		}

		@Override
		public boolean isAccountNonExpired() {
			return true;
		}

		@Override
		public boolean isAccountNonLocked() {
			return true;
		}

		@Override
		public boolean isCredentialsNonExpired() {
			return true;
		}

		@Override
		public boolean isEnabled() {
			return true;
		}

		private static final long serialVersionUID = 5639683223516504866L;

	}
}
