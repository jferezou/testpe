package com.poleemploi;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.poleemploi.service.TransformService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=TestConfig.class)
public class TransformServiceTest {
	@Autowired
	private TransformService transformService;
	
	
	@Test
	public void testVersJavanais() {
		assertThat(this.transformService.convertToJavanais("bonjour")).isEqualTo("bavonjavour");
		assertThat(this.transformService.convertToJavanais("chante")).isEqualTo("chavantave");
		assertThat(this.transformService.convertToJavanais("moyen")).isEqualTo("mavoyen");
		assertThat(this.transformService.convertToJavanais("exemple")).isEqualTo("avexavemplave");
		assertThat(this.transformService.convertToJavanais("au")).isEqualTo("avau");
	}
	

}
