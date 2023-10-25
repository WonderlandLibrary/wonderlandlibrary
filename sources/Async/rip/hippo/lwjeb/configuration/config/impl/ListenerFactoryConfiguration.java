package rip.hippo.lwjeb.configuration.config.impl;

import rip.hippo.lwjeb.configuration.config.Configuration;
import rip.hippo.lwjeb.configuration.invocation.ListenerFactory;
import rip.hippo.lwjeb.configuration.invocation.impl.DirectListenerFactory;


/**
 * @author Hippo
 * @version 1.0.0, 3/18/21
 * @since 5.2.0
 * <p>
 * The listener factory configuration allows you to configure how your listeners are created.
 * </p>
 */
public final class ListenerFactoryConfiguration implements Configuration<ListenerFactoryConfiguration> {

  /**
   * The listener factory.
   */
  private ListenerFactory listenerFactory;

  /**
   * Static constructor wrapper to create the default config.
   *
   * @return The default config.
   */
  public static ListenerFactoryConfiguration getDefault() {
    return new ListenerFactoryConfiguration().provideDefault();
  }

  /**
   * Sets the default config values.
   *
   * @return The default config.
   */
  @Override
  public ListenerFactoryConfiguration provideDefault() {
    this.setListenerFactory(new DirectListenerFactory());
    return this;
  }

  /**
   * Gets the listener factory.
   *
   * @return The listener factory.
   */
  public ListenerFactory getListenerFactory() {
    return listenerFactory;
  }

  /**
   * Sets the listener factory.
   *
   * @param listenerFactory The new listener factory.
   */
  public void setListenerFactory(ListenerFactory listenerFactory) {
    this.listenerFactory = listenerFactory;
  }
}
