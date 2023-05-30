# POC - Système d'intervention d'urgence en temps réel

Le système d'intervention d'urgence en temps réel a pour objectif de démontrer la capacité du 
système à trouver l'hôpital le plus proche ayant des lits disponibles, même en cas de charge important. 

## Installation

Installez [Maven](https://maven.apache.org/) sur votre système si ce n'est pas déjà fait, puis clonez le 
projet :

```bash
git clone https://github.com/BastienLB/LB_OC_P11_POC.git
```

## Utilisation

```bash
~$ cd LB_OC_P11_POC/

~$ mvn spring-boot:run
```
Le service sera accessible à l'adresse : [http://localhost:9000](http://localhost:9000)

Les endpoints disponibles sont les suivants :

* **[GET] /hospitals** : renvoie l'intégralité des hôpitaux en base de données


* **[GET] /hospitals/{id}** : renvoie un hôpital précis. 
  * ID : Long Integer
 

* **[GET] /hospitals/{latitude}/{longitude}** : renvoie l'hôpital le plus proche de latitude et longitude.
  * latitude: long double
  * longitude: long double


## License

[MIT](https://choosealicense.com/licenses/mit/)