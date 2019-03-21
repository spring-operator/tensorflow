/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.cloud.stream.app.tensorflow.processor;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.util.EnvironmentTestUtils;
import org.springframework.cloud.stream.config.SpelExpressionConverterConfiguration;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author Christian Tzolov
 * @author Artem Bilan
 */
public class TensorflowProcessorPropertiesTests {

	private AnnotationConfigApplicationContext context;

	@Before
	public void beforeTest() {
		context = new AnnotationConfigApplicationContext();
		EnvironmentTestUtils.addEnvironment(context, "tensorflow.model:NONE");
		EnvironmentTestUtils.addEnvironment(context, "tensorflow.modelFetch:NONE");
	}

	@After
	public void asfterTest() {
		context.close();
	}

	@Test
	public void modelCanBeCustomized() {
		EnvironmentTestUtils.addEnvironment(context, "tensorflow.model:/remote");
		context.register(Conf.class);
		context.refresh();
		TensorflowCommonProcessorProperties properties = context.getBean(TensorflowCommonProcessorProperties.class);
		assertThat(properties.getModel(), equalTo(context.getResource("/remote")));
	}

	@Test
	public void modelFetchCanBeCustomized() {
		EnvironmentTestUtils.addEnvironment(context, "tensorflow.modelFetch:output1");
		context.register(Conf.class);
		context.refresh();
		TensorflowCommonProcessorProperties properties = context.getBean(TensorflowCommonProcessorProperties.class);
		assertThat(properties.getModelFetch(), equalTo("output1"));
	}

	@Test
	public void modelFetchIndexCanBeCustomized() {
		EnvironmentTestUtils.addEnvironment(context, "tensorflow.modelFetchIndex:666");
		context.register(Conf.class);
		context.refresh();
		TensorflowCommonProcessorProperties properties = context.getBean(TensorflowCommonProcessorProperties.class);
		assertThat(properties.getModelFetchIndex(), equalTo(666));
	}

	@Test
	public void modeDefaultsToPayload() {
		context.register(Conf.class);
		context.refresh();
		TensorflowCommonProcessorProperties properties = context.getBean(TensorflowCommonProcessorProperties.class);
		assertThat(properties.getMode(), equalTo(OutputMode.payload));
	}

	@Test
	public void modeCanBeCustomized() {
		EnvironmentTestUtils.addEnvironment(context, "tensorflow.mode:header");
		context.register(Conf.class);
		context.refresh();
		TensorflowCommonProcessorProperties properties = context.getBean(TensorflowCommonProcessorProperties.class);
		assertThat(properties.getMode(), equalTo(OutputMode.header));
	}

	@Test
	public void emptyOutputNameDefaultsTomodelFetch() {
		EnvironmentTestUtils.addEnvironment(context, "tensorflow.modelFetch:output1");
		context.register(Conf.class);
		context.refresh();
		TensorflowCommonProcessorProperties properties = context.getBean(TensorflowCommonProcessorProperties.class);
		assertThat(properties.getOutputName(), equalTo("output1"));
	}

	@Test
	public void outputNameCanBeCustomized() {
		EnvironmentTestUtils.addEnvironment(context, "tensorflow.outputName:outputName2");
		context.register(Conf.class);
		context.refresh();
		TensorflowCommonProcessorProperties properties = context.getBean(TensorflowCommonProcessorProperties.class);
		assertThat(properties.getOutputName(), equalTo("outputName2"));
	}

	@Test
	public void expressionDefaultsToNull() {
		context.register(Conf.class);
		context.refresh();
		TensorflowCommonProcessorProperties properties = context.getBean(TensorflowCommonProcessorProperties.class);
		assertNull(properties.getExpression());
	}

	@Test
	public void expressionCanBeCustomized() {
		EnvironmentTestUtils.addEnvironment(context, "tensorflow.expression:header");
		context.register(Conf.class);
		context.refresh();
		TensorflowCommonProcessorProperties properties = context.getBean(TensorflowCommonProcessorProperties.class);
		assertThat(properties.getExpression().getExpressionString(), equalTo("header"));
	}

	@Configuration
	@EnableConfigurationProperties(TensorflowCommonProcessorProperties.class)
	@Import(SpelExpressionConverterConfiguration.class)
	static class Conf {

	}

}
