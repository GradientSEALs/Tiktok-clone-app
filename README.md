Υλοποίηση σε Android μιας εφαρμογής τύπου TikTok

Επιλέξαμε να υλοποιήσουμε την εφαρμογή μας χρησιμοποιώντας Fragments. Το βασικό
Activity είναι το MainActivity όπου κάνει extend το AppCompatActivity και ανοίγει την
σύνδεση μέσω Sockets στην διεύθυνση 192.168.1.4 και θύρα 4000. Κάνει implement το
interface AsyncTask για να μπορέσουμε να χρησιμοποιήσουμε παραλληλία.
Στο TikTokactivity, αρχικά δημιουργούμε το activity, αρχικοποιούμε τα πεδία username και
port. Φτιάχνουμε μία ArrayList που περιέχει τις διευθύνσεις και τα ports των brokers.
Φτιάχνουμε εν συνεχεία ένα Directory με την μέθοδο cretateDirectory() όπου θα
αποθηκεύονται τα videos που θα γίνονται upload. Το directory αυτό δημιουργείται στην
εξωτερική μνήμη του κινητού. Στην κλάση Server, δημιουργούμε ένα thread για κάθε νέα
σύνδεση ενός Consumer και στην κλάση Consumer δημιουργούμε τα αντικείμενα VideoFile
και File. Τέλος, ανοίγουμε την σύνδεση με χρήση Socket.

Επί προσθέτως, η εφαρμογή μας προσφέρει την δυνατότητα του Upload. Αρχικά κάνουμε
hash τις διευθύνσεις και τα ports των brokers χρησιμοποιώντας την συνάρτηση
κατακερματισμού ModM5. Ο χρήστης μπορεί είτε να κάνει capture ένα βίντεο και να το
ανεβάσει, είτε να επιλέξει αμέσως ένα από την συλλογή του. Η κλάση Publisher κάνει
extend το AsyncTask και μέσω της doInBackground() ανοίγει μία Socket σύνδεση στην
διεύθυνση 192.168.1.4 στέλνοντας το video και τα hashtags. Το video στέλνεται σε chunks.
Η μέθοδος doInBackground της μεθόδου NamePublisher, παρόμοια με της Publisher,
στέλνει το video και το channelname.

Ένα άλλο feature που προσφέρεται είναι το Subscription. Ένας χρήστης μπορεί να κάνει
subscribe χρησιμοποιώντας είτε το channelname είτε κάποιο hashtag. Η κλάση Subscribe με
την κλήση THREAD_POOL_EXECUTOR του AsyncTask, τρέχει πολλά παράλληλα threads.
Τέλος, η κλάση Subscriber χρησιμοποιεί Sockets για να πραγματοποιήσει το subscribe στην
διεύθυνση 192.168.1.4.

Αξίζει να σημειώσουμε ότι υλοποιήσαμε και ένα ιδιαίτερο feature, το Livefeed. Σε αυτό το
fragment εμφανίζεται μία ροή των video που έχουν γίνει upload.