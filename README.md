# firebaseApp
##define your database rules as follows##
{
  "rules": {
    ".read": true,
    ".write": true
  }
}


##define your storage rules as follows##  
rules_version = '2';
service firebase.storage {
  match /b/{bucket}/o {
    match /{allPaths=**} {
      allow read, write: if request.auth != null;
    }
  }
}
