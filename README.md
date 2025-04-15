<h1 align="center">🔌 Projet Enerdis – Communication Modbus RTU via RS485 /h1>


![Java](https://img.shields.io/badge/Java-%23181717.svg?style=for-the-badge&logo=java&logoColor=white)
![Modbus RTU](https://img.shields.io/badge/Modbus%20RTU-Protocol-blue?style=for-the-badge)
![RS485](https://img.shields.io/badge/RS485-Serial-green?style=for-the-badge)
![Status](https://img.shields.io/badge/Status-En%20cours-yellow?style=for-the-badge)


## 🎯 Objectif

Ce projet a pour but de mettre en place une communication série avec un **capteur Enerdis** via le protocole **Modbus RTU** sur une liaison **RS485**.  
L’objectif principal est de pouvoir **interroger le capteur** pour récupérer des données (tension, intensité, puissance, etc.), les **décoder** et les **afficher dans un site web**.

---

## ⚙️ Matériel utilisé

- 🧭 **Capteur** : Enerdis (ex: COUNTIS E44)
- 🔌 **Interface série** : Convertisseur USB ↔ RS485
- 🖥️ **Ordinateur** : Poste de développement
- 🔧 **Câblage** : Bus RS485 (connexion A/B correcte)

---

## 🛠️ Outils & Langages

- 💻 **Langage** : Java  
- 🧠 **IDE** : IntelliJ IDEA / VSCode  
- 📚 **Librairies** :
  - `javax.comm` ou `jSerialComm` pour la communication série
  - Classes personnalisées pour le calcul CRC, l’encodage/décodage des trames

---

## 📦 Structure du code

### `LiaisonSerie.java`
➡ Gère l’ouverture/configuration du port série, envoie et réception des trames.

### `CRC16.java`
➡ Calcule le **checksum CRC16** pour garantir l’intégrité des trames Modbus.

### `BigEndian.java`
➡ Convertit les **octets Big Endian** en types exploitables (`int`, `float`, etc.).

### `In.java`
➡ Génère et envoie une **trame d’interrogation Modbus**, puis lit la réponse.

---

## 🔁 Exemple de Trame

[01] [03] [00] [00] [00] [01] [84] [0A]   <-- Trame envoyée

[01] [03] [02] [09] [10] [BF] [D8]  <-- Réponse du capteur
