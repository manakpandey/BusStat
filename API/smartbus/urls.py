from django.urls import path
from smartbus import views

urlpatterns = [
    path('bus/', views.bus_list),
    path('bus/<int:pk>/', views.bus_detail),
    path('bus/search/src=<src>&dest=<dest>/', views.bus_search),
    path('stops/', views.stops_list),
    path('stops/<pk>/', views.stops_detail),
    path('user/add/', views.add_user),
    path('user/ops/<pk>/', views.user_ops),
    path('user/verify/', views.verify_user),
    path('token/generate/', views.generate_token),
    path('token/verify/<pk>/', views.use_token),
]