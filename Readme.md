
```
## TO Start Project
### np network create Chanel and also deploy the deploy the chainCode....
./network.sh up createChannel -ca && ./network.sh deployCC 
```


```bash
http POST :8181/api/v1/assets/init
http  GET :8181/api/v1/assets/all
http  GET  :8181/api/v1/assets/asset6
http POST :8181/api/v1/assets/create  Color="yellow" Owner="kang" Size:=5 AppraisedValue:=100 
http PUT :8181/api/v1/assets/update/asset6  Color="yellow" Owner="kang" Size:=5 AppraisedValue:=100 
http Delete :8181/api/v1/assets/asset6

```

```
## To Testing with the Create asset with httpie 

 http POST http://localhost:8181/api/v1/assets/create Content-Type:application/json Color="yellow" Owner="kang" Size:=5 AppraisedValue:=100 

```
