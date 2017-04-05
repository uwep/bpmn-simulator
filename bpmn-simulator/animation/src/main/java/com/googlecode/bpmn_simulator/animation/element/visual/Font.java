/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.googlecode.bpmn_simulator.animation.element.visual;

public class Font {

	private final String name;
	private final Double size;
	private final boolean bold;
	private final boolean italic;
	private final boolean underline;
	private final boolean strikeThrough;

	public Font(final String name, final Double size,
			final boolean bold, final boolean italic,
			final boolean underline, final boolean strikeThrough) {
		super();
		this.name = name;
		this.size = size;
		this.bold = bold;
		this.italic = italic;
		this.underline = underline;
		this.strikeThrough = strikeThrough;
	}

	public String getName() {
		return name;
	}

	public Double getSize() {
		return size;
	}

	public boolean isBold() {
		return bold;
	}

	public boolean isItalic() {
		return italic;
	}

	public boolean isUnderline() {
		return underline;
	}

	public boolean isStrikeThrough() {
		return strikeThrough;
	}

}
