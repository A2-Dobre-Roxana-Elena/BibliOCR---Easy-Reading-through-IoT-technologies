package com.canhub.cropper.sample.bazadedate

import com.google.firebase.firestore.FirebaseFirestore

class BazaDeDate {

  private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

  // Funcție generală pentru a genera un ID auto-incrementat
  private fun generateAutoIncrementId(metaDocument: String, fieldName: String, onSuccess: (Long) -> Unit, onFailure: (Exception) -> Unit) {
    val metaDocRef = db.collection("ids").document(metaDocument)

    db.runTransaction { transaction ->
      val snapshot = transaction.get(metaDocRef)
      val newId = snapshot.getLong(fieldName)?.plus(1) ?: 1

      // Actualizează noul ID în documentul meta
      transaction.update(metaDocRef, fieldName, newId)
      newId
    }.addOnSuccessListener { newId ->
      onSuccess(newId)
    }.addOnFailureListener { e ->
      onFailure(e)
    }
  }

  // Adăugarea unei cărți în Firestore cu auto-incrementare a ID-ului
  fun addBookWithAutoIncrement(book: Carte, onSuccess: (String) -> Unit, onFailure: (Exception) -> Unit) {
    generateAutoIncrementId(
      "idCarte", "idUltimaCarte",
      { newId ->
        val bookWithId = book.copy(id = newId.toString())
        val newBookDocRef = db.collection("carti").document(newId.toString())
        newBookDocRef.set(bookWithId)
          .addOnSuccessListener { onSuccess(newId.toString()) }
          .addOnFailureListener { e -> onFailure(e) }
      },
      onFailure,
    )
  }

  // Adăugarea unei instanțe de carte cu auto-incrementare a ID-ului
  fun addBookInstanceWithAutoIncrement(bookInstance: InstantaCarte, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
    generateAutoIncrementId(
      "idInstantaCarte", "idUltimaInstantaCarte",
      { newId ->
        val bookInstanceWithId = bookInstance.copy(id = newId.toString())
        val newBookInstanceDocRef = db.collection("instanteCarti").document(newId.toString())
        newBookInstanceDocRef.set(bookInstanceWithId)
          .addOnSuccessListener { onSuccess() }
          .addOnFailureListener { e -> onFailure(e) }
      },
      onFailure,
    )
  }

  // Adăugarea unui utilizator cu auto-incrementare a ID-ului
//  fun addUserWithAutoIncrement(user: User, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
//    generateAutoIncrementId("idUtilizator", "idUltimaUtilizator", { newId ->
//      val userWithId = user.copy(id = newId.toString())
//      val newUserDocRef = db.collection("users").document(newId.toString())
//      newUserDocRef.set(userWithId)
//        .addOnSuccessListener { onSuccess() }
//        .addOnFailureListener { e -> onFailure(e) }
//    }, onFailure)
//  }
//
//  // Adăugarea unui citat cu auto-incrementare a ID-ului
//  fun addQuoteWithAutoIncrement(quote: Quote, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
//    generateAutoIncrementId("idCitat", "idUltimaCitatt", { newId ->
//      val quoteWithId = quote.copy(id = newId.toString())
//      val newQuoteDocRef = db.collection("quotes").document(newId.toString())
//      newQuoteDocRef.set(quoteWithId)
//        .addOnSuccessListener { onSuccess() }
//        .addOnFailureListener { e -> onFailure(e) }
//    }, onFailure)
//  }


  // Adăugarea unui utilizator cu auto-incrementare a ID-ului
  fun addUserWithAutoIncrement(user: Utilizatori, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
    generateAutoIncrementId(
      "idUtilizator", "idUltimaUtilizator",
      { newId ->
        val userWithId = user.copy(id = newId.toString())
        val newUserDocRef = db.collection("utilizatori").document(newId.toString())
        newUserDocRef.set(userWithId)
          .addOnSuccessListener { onSuccess() }
          .addOnFailureListener { e -> onFailure(e) }
      },
      onFailure,
    )
  }

  // Adăugarea unui citat cu auto-incrementare a ID-ului
  fun addQuoteWithAutoIncrement(quote: Citate, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
    generateAutoIncrementId(
      "idCitat", "idUltimaCitatt",
      { newId ->
        val quoteWithId = quote.copy(id = newId.toString())
        val newQuoteDocRef = db.collection("citate").document(newId.toString())
        newQuoteDocRef.set(quoteWithId)
          .addOnSuccessListener { onSuccess() }
          .addOnFailureListener { e -> onFailure(e) }
      },
      onFailure,
    )
  }

  // Verificarea dacă o carte există deja în Firestore
  fun checkIfBookExists(book: Carte, onSuccess: (String?) -> Unit, onFailure: (Exception) -> Unit) {
    db.collection("carti")
      .whereEqualTo("titlu", book.titlu)
      .whereEqualTo("autor", book.autor)
      .get()
      .addOnSuccessListener { result ->
        if (result.isEmpty) {
          onSuccess(null)
        } else {
          onSuccess(result.documents[0].id)
        }
      }
      .addOnFailureListener { e -> onFailure(e) }
  }




  //// autoincrement
  // Funcție generală pentru a genera un ID auto-incrementat
//  private fun generateAutoIncrementId(metaDocument: String, fieldName: String, onSuccess: (Long) -> Unit, onFailure: (Exception) -> Unit) {
//    val metaDocRef = db.collection("ids").document(metaDocument)
//
//    db.runTransaction { transaction ->
//      val snapshot = transaction.get(metaDocRef)
//      val newId = snapshot.getLong(fieldName)?.plus(1) ?: 1
//
//      // Actualizează noul ID în documentul meta
//      transaction.update(metaDocRef, fieldName, newId)
//      newId
//    }.addOnSuccessListener { newId ->
//      onSuccess(newId)
//    }.addOnFailureListener { e ->
//      onFailure(e)
//    }
//  }
//
//  // Adăugarea unei cărți în Firestore cu auto-incrementare a ID-ului
//  fun addBookWithAutoIncrement(book: Carte, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
//    generateAutoIncrementId("idCarte", "idUltimaCarte", { newId ->
//      val bookWithId = book.copy(id = newId.toString())
//      val newBookDocRef = db.collection("carti").document(newId.toString())
//      newBookDocRef.set(bookWithId)
//        .addOnSuccessListener { onSuccess() }
//        .addOnFailureListener { e -> onFailure(e) }
//    }, onFailure)
//  }
//



//  // Add a book to Firestore
//  fun addBook(book: Carte, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
//    db.collection("carti")
//      .add(book)
//      .addOnSuccessListener { onSuccess() }
//      .addOnFailureListener { e -> onFailure(e) }
//  }

  // Fetch all books from Firestore
  fun fetchBooks(onSuccess: (List<Carte>) -> Unit, onFailure: (Exception) -> Unit) {
    db.collection("carti")
      .get()
      .addOnSuccessListener { result ->
        val books = result.map { document -> document.toObject(Carte::class.java) }
        onSuccess(books)
      }
      .addOnFailureListener { e -> onFailure(e) }
  }

  fun fetchCarteTitlu(idCarte: String, onSuccess: (Carte) -> Unit, onFailure: (Exception) -> Unit) {
    db.collection("carti")
      .document(idCarte)
      .get()
      .addOnSuccessListener { document ->
        if (document != null && document.exists()) {
          val carte = document.toObject(Carte::class.java)
          if (carte != null) {
            onSuccess(carte)
          } else {
            onFailure(Exception("Cartea nu a putut fi convertită"))
          }
        } else {
          onFailure(Exception("Cartea nu există"))
        }
      }
      .addOnFailureListener { e -> onFailure(e) }
  }

  fun fetchCitatTitlu(idCitat: String, onSuccess: (Citate) -> Unit, onFailure: (Exception) -> Unit) {
    db.collection("citate")
      .document(idCitat)
      .get()
      .addOnSuccessListener { document ->
        if (document != null && document.exists()) {
          val citat = document.toObject(Citate::class.java)
          if (citat != null) {
            onSuccess(citat)
          } else {
            onFailure(Exception("Citatul nu a putut fi convertit"))
          }
        } else {
          onFailure(Exception("Citatul nu există"))
        }
      }
      .addOnFailureListener { e -> onFailure(e) }
  }

  fun fetchUser(idUser: String, onSuccess: (Utilizatori) -> Unit, onFailure: (Exception) -> Unit) {
    db.collection("utilizatori")
      .whereEqualTo("id", idUser)
      .get()
      .addOnSuccessListener { documents ->
        if (documents != null && !documents.isEmpty) {
          val document = documents.documents[0]
          val utilizator = document.toObject(Utilizatori::class.java)
          if (utilizator != null) {
            onSuccess(utilizator)
          } else {
            onFailure(Exception("Utilizatorul nu a putut fi convertit"))
          }
        } else {
          onFailure(Exception("Utilizatorul nu există"))
        }
      }
      .addOnFailureListener { e -> onFailure(e) }
  }


  fun fetchUserName(idUserName: String, onSuccess: (Utilizatori) -> Unit, onFailure: (Exception) -> Unit) {
    db.collection("utilizatori")
      .whereEqualTo("numeUtilizator", idUserName)
      .get()
      .addOnSuccessListener { documents ->
        if (documents != null && !documents.isEmpty) {
          val document = documents.documents[0]
          val utilizator = document.toObject(Utilizatori::class.java)
          if (utilizator != null) {
            onSuccess(utilizator)
          } else {
            onFailure(Exception("Utilizatorul nu a putut fi convertit"))
          }
        } else {
          onFailure(Exception("Utilizatorul nu există"))
        }
      }
      .addOnFailureListener { e -> onFailure(e) }
  }

//  fun fetchUser(idUser: String, onSuccess: (Utilizatori) -> Unit, onFailure: (Exception) -> Unit) {
//    db.collection("utilizatori")
//      .whereEqualTo("idUser", idUser)
//      .get()
//      .addOnSuccessListener { document ->
//        if (document != null && document.exists()) {
//          val numeUtilizator = document.toObject(Utilizatori::class.java)
//          if (numeUtilizator != null) {
//            onSuccess(numeUtilizator)
//          } else {
//            onFailure(Exception("Utilizatorul nu a putut fi convertit"))
//          }
//        } else {
//          onFailure(Exception("Utilizatorul nu există"))
//        }
//      }
//      .addOnFailureListener { e -> onFailure(e) }
//  }

  fun fetchInstantaCarte(idCarte: String, onSuccess: (List<InstantaCarte>) -> Unit, onFailure: (Exception) -> Unit) {
    db.collection("instanteCarti")
      .whereEqualTo("idCarte", idCarte)
      .get()
      .addOnSuccessListener { result ->
        val instantaCarti = result.map { document -> document.toObject(InstantaCarte::class.java) }
        onSuccess(instantaCarti)
      }
      .addOnFailureListener { e -> onFailure(e) }
  }

//  fun fetchQuotes(onSuccess: (List<Citate>) -> Unit, onFailure: (Exception) -> Unit) {
//    db.collection("citate")
//      .get()
//      .addOnSuccessListener { result ->
//        val citate = result.map { document -> document.toObject(Citate::class.java) }
//        onSuccess(citate)
//      }
//      .addOnFailureListener { e -> onFailure(e) }
//  }

  fun fetchQuotes(onSuccess: (List<Citate>) -> Unit, onFailure: (Exception) -> Unit) {
    // Cod pentru a obține citatele private ale utilizatorului
    try {
      // Accesarea colecției "citate" din Firestore
      val db = FirebaseFirestore.getInstance()
      db.collection("citate")
        .whereEqualTo("public", true)  // Filtrarea citatelor după ID-ul utilizatorului
        .get()
        .addOnSuccessListener { result ->
          // Conversia documentelor în obiecte Citate
          val privateQuotes = result.documents.map { document -> document.toObject(Citate::class.java)!! }
          // Apelarea callback-ului onSuccess cu lista de citate private
          onSuccess(privateQuotes)
        }
        .addOnFailureListener { e ->
          // Apelarea callback-ului onFailure în caz de eroare
          onFailure(e)
        }
    } catch (e: Exception) {
      // Gestionarea excepțiilor și apelarea callback-ului onFailure
      onFailure(e)
    }
  }

  fun addImprumut(imprumut: Imprumut, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
    db.collection("imprumuturi")
      .get()
      .addOnSuccessListener { result ->
        val nextId = result.size() + 1
        val imprumutWithId = imprumut.copy(id = nextId.toString())
        db.collection("imprumuturi")
          .document(nextId.toString())
          .set(imprumutWithId)
          .addOnSuccessListener { onSuccess() }
          .addOnFailureListener { e -> onFailure(e) }
      }
      .addOnFailureListener { e -> onFailure(e) }
  }



  fun imprumutaCarte(idCarte: String, onComplete: (Boolean) -> Unit) {
    val db = FirebaseFirestore.getInstance()
    val carteRef = db.collection("instanteCarti").document(idCarte)

    carteRef.get().addOnSuccessListener { document ->
      if (document.exists()) {
        carteRef.update("eimprumatata", true)
          .addOnSuccessListener {
            onComplete(true)
          }
          .addOnFailureListener {
            onComplete(false)
          }
      } else {
        onComplete(false)
      }
    }.addOnFailureListener {
      onComplete(false)
    }
  }

  fun returneazaCarte(idCarte: String, onComplete: (Boolean) -> Unit) {
    val db = FirebaseFirestore.getInstance()
    val carteRef = db.collection("instanteCarti").document(idCarte)

    carteRef.get().addOnSuccessListener { document ->
      if (document.exists()) {
        carteRef.update("eimprumatata", false)
          .addOnSuccessListener {
            onComplete(true)
          }
          .addOnFailureListener {
            onComplete(false)
          }
      } else {
        onComplete(false)
      }
    }.addOnFailureListener {
      onComplete(false)
    }
  }

  fun fetchPrivateQuotes(userId: String, onSuccess: (List<Citate>) -> Unit, onFailure: (Exception) -> Unit) {
    // Cod pentru a obține citatele private ale utilizatorului
    try {
      // Accesarea colecției "citate" din Firestore
      val db = FirebaseFirestore.getInstance()
      db.collection("citate")
        .whereEqualTo("idUser", userId)  // Filtrarea citatelor după ID-ul utilizatorului
        .whereEqualTo("public", false)  // Filtrarea citatelor după ID-ul utilizatorului
        .get()
        .addOnSuccessListener { result ->
          // Conversia documentelor în obiecte Citate
          val privateQuotes = result.documents.map { document -> document.toObject(Citate::class.java)!! }
          // Apelarea callback-ului onSuccess cu lista de citate private
          onSuccess(privateQuotes)
        }
        .addOnFailureListener { e ->
          // Apelarea callback-ului onFailure în caz de eroare
          onFailure(e)
        }
    } catch (e: Exception) {
      // Gestionarea excepțiilor și apelarea callback-ului onFailure
      onFailure(e)
    }
  }


}
