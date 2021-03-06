package eu.telecomnancy.amio.notification.rules;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Fact;
import org.jeasy.rules.api.Facts;

import eu.telecomnancy.amio.notification.contexts.EventContext;
import eu.telecomnancy.amio.notification.dispatchers.EventDispatcher;
import eu.telecomnancy.amio.notification.contexts.NotificationContext;
import eu.telecomnancy.amio.notification.flags.NotificationType;
import eu.telecomnancy.amio.notification.dispatchers.NotificationDispatcher;

/**
 * Define a generic rule to be checked by the rule engine and the event dispatcher
 *
 * @see EventDispatcher
 */
public abstract class RuleBase implements IRule {

    @Condition
    public final boolean when(@Fact(EventDispatcher.ContextTag) EventContext context) {
        return isActiveWhen(context);
    }

    @Action
    @Override
    public void then(Facts facts) {
        EventContext context = (EventContext) facts
                .getFact(EventDispatcher.ContextTag)
                .getValue();

        NotificationContext notificationContext =
                new NotificationContext(getNotificationTargets(), context);

        NotificationDispatcher.sendNotificationsFrom(notificationContext);
    }

    /**
     * Define which type of notification to be triggered when the rule is valid
     *
     * @return Type of notification to be triggered when the rule is valid
     */
    protected abstract NotificationType getNotificationTargets();

}
