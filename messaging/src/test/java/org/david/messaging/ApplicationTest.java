package org.david.messaging;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@Import(Application.class)
public class ApplicationTest {

   @Test
   public void shouldStartApp() {
   }
}
