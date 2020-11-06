package eu.telecomnancy.amio.notification.notifiers;

import eu.telecomnancy.amio.notification.NotificationContext;
import eu.telecomnancy.amio.notification.annotations.EventNotifier;
import eu.telecomnancy.amio.notification.flags.NotificationType;

@EventNotifier(target = NotificationType.ANDROID)
public class ToastNotifier extends AndroidNotifier {

    public ToastNotifier(NotificationContext payload) {
        super(payload);
    }

    @Override
    public void sendNotification() {
        System.out.println("Toast from " + VibratorNotifier.class.getSimpleName());
    }

}
