package org.pentaho.di.trans.steps.zendesk;

import java.util.Date;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import org.pentaho.di.core.logging.LogChannelInterface;
import org.pentaho.di.i18n.BaseMessages;
import org.pentaho.di.trans.step.BaseStep;
import org.zendesk.client.v2.Zendesk;
import org.zendesk.client.v2.ZendeskResponseRateLimitException;
import org.zendesk.client.v2.model.Audit;
import org.zendesk.client.v2.model.Field;
import org.zendesk.client.v2.model.Group;
import org.zendesk.client.v2.model.GroupMembership;
import org.zendesk.client.v2.model.Identity;
import org.zendesk.client.v2.model.Organization;
import org.zendesk.client.v2.model.SuspendedTicket;
import org.zendesk.client.v2.model.Ticket;
import org.zendesk.client.v2.model.User;
import org.zendesk.client.v2.model.hc.Article;
import org.zendesk.client.v2.model.hc.Category;
import org.zendesk.client.v2.model.hc.Section;
import org.zendesk.client.v2.model.hc.Translation;

public class ZendeskFacade {
  static Class<?> PKG = ZendeskInput.class;

  final LogChannelInterface log;
  Zendesk conn;

  public ZendeskFacade( BaseStep parent ) {
    log = parent.getLogChannel();
  }

  public boolean isConnected() {
    return conn != null && !conn.isClosed();
  }

  public void close() {
    if ( conn != null ) {
      conn.close();
    }
  }

  // Used for testing purposes
  protected Zendesk.Builder getBuilder( String url ) {
    return new Zendesk.Builder( url );
  }

  public void connect( String subDomain, String username, String password, boolean token ) {
    if ( conn != null ) {
      log.logDebug( "Closing existing Zendesk connection and creating new connection." );
      conn.close();
    }
    Zendesk.Builder builder = getBuilder( String.format( "https://%s.zendesk.com", subDomain ) );

    if ( username.contains( "/token" ) ) {
      token = true;
      username = username.replaceAll( "/token", "" );
      log.logDetailed( BaseMessages.getString( PKG, "ZendeskInput.UsernameContainsToken.Warning" ) );
    }
    builder.setUsername( username );

    if ( token ) {
      builder.setToken( password );
    } else {
      builder.setPassword( password );
    }
    conn = builder.build();
  }

  private void handleRateLimit( ZendeskResponseRateLimitException zre ) {
    if ( zre == null ) {
      return;
    }
    Long retryAfter = zre.getRetryAfter();
    log.logDetailed( BaseMessages.getString( PKG, "ZendeskInput.Info.RateLimited", retryAfter ) );
    try {
      TimeUnit.SECONDS.sleep( retryAfter );
    } catch ( InterruptedException ignore ) {
      // Consider we have slept enough. The api should tell us how much to wait
    }
  }

  public Iterable<Group> getGroups() {
    Iterable<Group> apiResult = null;
    while ( true ) {
      try {
        apiResult = conn.getGroups();
        break;
      } catch ( ZendeskResponseRateLimitException zre ) {
        handleRateLimit( zre );
      }
    }
    return new ZendeskIterable<Group>( apiResult );
  }

  public Iterable<GroupMembership> getGroupMemberships() {
    Iterable<GroupMembership> apiResult = null;
    while ( true ) {
      try {
        apiResult = conn.getGroupMemberships();
        break;
      } catch ( ZendeskResponseRateLimitException zre ) {
        handleRateLimit( zre );
      }
    }
    return new ZendeskIterable<GroupMembership>( apiResult );
  }

  public Organization getOrganization( Long organizationId ) {
    Organization apiResult = null;
    while ( true ) {
      try {
        apiResult = conn.getOrganization( organizationId );
        break;
      } catch ( ZendeskResponseRateLimitException zre ) {
        handleRateLimit( zre );
      }
    }
    return apiResult;
  }

  public Iterable<Organization> getOrganizations() {
    Iterable<Organization> apiResult = null;
    while ( true ) {
      try {
        apiResult = conn.getOrganizations();
        break;
      } catch ( ZendeskResponseRateLimitException zre ) {
        handleRateLimit( zre );
      }
    }
    return new ZendeskIterable<Organization>( apiResult );
  }

  public Iterable<Audit> getTicketAudits( Long ticketId ) {
    Iterable<Audit> apiResult = null;
    while ( true ) {
      try {
        apiResult = conn.getTicketAudits( ticketId );
        break;
      } catch ( ZendeskResponseRateLimitException zre ) {
        handleRateLimit( zre );
      }
    }
    return new ZendeskIterable<Audit>( apiResult );
  }

  public Iterable<SuspendedTicket> getSuspendedTickets() {
    Iterable<SuspendedTicket> apiResult = null;
    while ( true ) {
      try {
        apiResult = conn.getSuspendedTickets();
        break;
      } catch ( ZendeskResponseRateLimitException zre ) {
        handleRateLimit( zre );
      }
    }
    return new ZendeskIterable<SuspendedTicket>( apiResult );
  }

  public void deleteSuspendedTicket( Long ticketId ) {
    while ( true ) {
      try {
        conn.deleteSuspendedTicket( ticketId );
        return;
      } catch ( ZendeskResponseRateLimitException zre ) {
        handleRateLimit( zre );
      }
    }
  }

  public Iterable<Field> getTicketFields() {
    Iterable<Field> apiResult = null;
    while ( true ) {
      try {
        apiResult = conn.getTicketFields();
        break;
      } catch ( ZendeskResponseRateLimitException zre ) {
        handleRateLimit( zre );
      }
    }
    return new ZendeskIterable<Field>( apiResult );
  }

  public User getUser( Long userId ) {
    User apiResult = null;
    while ( true ) {
      try {
        apiResult = conn.getUser( userId );
        break;
      } catch ( ZendeskResponseRateLimitException zre ) {
        handleRateLimit( zre );
      }
    }
    return apiResult;
  }

  public Iterable<User> getUsers() {
    Iterable<User> apiResult = null;
    while ( true ) {
      try {
        apiResult = conn.getUsers();
        break;
      } catch ( ZendeskResponseRateLimitException zre ) {
        handleRateLimit( zre );
      }
    }
    return new ZendeskIterable<User>( apiResult );
  }

  public Iterable<Identity> getUserIdentities( Long userId ) {
    Iterable<Identity> apiResult = null;
    while ( true ) {
      try {
        apiResult = conn.getUserIdentities( userId );
        break;
      } catch ( ZendeskResponseRateLimitException zre ) {
        handleRateLimit( zre );
      }
    }
    return new ZendeskIterable<Identity>( apiResult );
  }

  private User updateUser( User user ) {
    while ( true ) {
      try {
        return conn.updateUser( user );
      } catch ( ZendeskResponseRateLimitException zre ) {
        handleRateLimit( zre );
      }
    }
  }

  public User suspendUser( Long userId ) {
    User user = new User();
    user.setId( userId );
    user.setSuspended( true );
    return updateUser( user );
  }

  public User unsuspendUser( Long userId ) {
    User user = new User();
    user.setId( userId );
    user.setSuspended( false );
    return updateUser( user );
  }

  public Iterable<Article> getHelpCenterArticles() {
    Iterable<Article> apiResult = null;
    while ( true ) {
      try {
        apiResult = conn.getArticles();
        break;
      } catch ( ZendeskResponseRateLimitException zre ) {
        handleRateLimit( zre );
      }
    }
    return new ZendeskIterable<Article>( apiResult );
  }

  public Article getHelpCenterArticle( Long articleId ) {
    while ( true ) {
      try {
        return conn.getArticle( articleId );
      } catch ( ZendeskResponseRateLimitException zre ) {
        handleRateLimit( zre );
      }
    }
  }

  public Iterable<Translation> getHelpCenterArticleTranslations( Long articleId ) {
    Iterable<Translation> apiResult = null;
    while ( true ) {
      try {
        apiResult = conn.getArticleTranslations( articleId );
        break;
      } catch ( ZendeskResponseRateLimitException zre ) {
        handleRateLimit( zre );
      }
    }
    return new ZendeskIterable<Translation>( apiResult );
  }

  public Iterable<Section> getHelpCenterSections() {
    Iterable<Section> apiResult = null;
    while ( true ) {
      try {
        apiResult = conn.getSections();
        break;
      } catch ( ZendeskResponseRateLimitException zre ) {
        handleRateLimit( zre );
      }
    }
    return new ZendeskIterable<Section>( apiResult );
  }

  public Iterable<Category> getHelpCenterCategories() {
    Iterable<Category> apiResult = null;
    while ( true ) {
      try {
        apiResult = conn.getCategories();
        break;
      } catch ( ZendeskResponseRateLimitException zre ) {
        handleRateLimit( zre );
      }
    }
    return new ZendeskIterable<Category>( apiResult );
  }

  public Iterable<Ticket> getTicketsIncrementally( Date startDate ) {
    Iterable<Ticket> apiResult = null;
    while ( true ) {
      try {
        apiResult = conn.getTicketsIncrementally( startDate );
        break;
      } catch ( ZendeskResponseRateLimitException zre ) {
        handleRateLimit( zre );
      }
    }
    return new ZendeskIterable<Ticket>( apiResult );
  }

  public Iterable<User> getUsersIncrementally( Date startDate ) {
    Iterable<User> apiResult = null;
    while ( true ) {
      try {
        apiResult = conn.getUsersIncrementally( startDate );
        break;
      } catch ( ZendeskResponseRateLimitException zre ) {
        handleRateLimit( zre );
      }
    }
    return new ZendeskIterable<User>( apiResult );
  }

  public Iterable<Organization> getOrganizationsIncrementally( Date startDate ) {
    Iterable<Organization> apiResult = null;
    while ( true ) {
      try {
        apiResult = conn.getOrganizationsIncrementally( startDate );
        break;
      } catch ( ZendeskResponseRateLimitException zre ) {
        handleRateLimit( zre );
      }
    }
    return new ZendeskIterable<Organization>( apiResult );
  }

  public Iterable<Article> getArticlesIncrementally( Date startDate ) {
    Iterable<Article> apiResult = null;
    while ( true ) {
      try {
        apiResult = conn.getArticlesIncrementally( startDate );
        break;
      } catch ( ZendeskResponseRateLimitException zre ) {
        handleRateLimit( zre );
      }
    }
    return new ZendeskIterable<Article>( apiResult );
  }

  private class ZendeskIterable<T> implements Iterable<T> {

    private final Iterable<T> delegate;
    private ZendeskIterable( Iterable<T> delegate ) {
      this.delegate = delegate;
    }

    @Override
    public Iterator<T> iterator() {
      return new ZendeskIterator( delegate.iterator() );
    }

    private class ZendeskIterator implements Iterator<T> {

      private final Iterator<T> delegate;
      private ZendeskIterator( Iterator<T> delegate ) {
        this.delegate = delegate;
      }

      @Override
      public boolean hasNext() {
        while ( true ) {
          try {
            return delegate.hasNext();
          } catch ( ZendeskResponseRateLimitException zre ) {
            Long retryAfter = zre.getRetryAfter();
            log.logDetailed( BaseMessages.getString( PKG, "ZendeskInput.Info.RateLimited", retryAfter ) );
            try {
              TimeUnit.SECONDS.sleep( retryAfter );
            } catch ( InterruptedException ignore ) {
              continue; // The API will tell us if we have not waited enough time
            }
          }
        }
      }

      @Override
      public T next() {
        return delegate.next();
      }
    }
  }
}
