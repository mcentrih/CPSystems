using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.XR.ARFoundation;

public class ARCursor : MonoBehaviour
{
    public GameObject objectToPlace;
    public GameObject ARCursorChild;

    public ARRaycastManager rayCastManager;

    public bool useCursor =true;

    // Start is called before the first frame update
    void Start()
    {
        ARCursorChild.SetActive(useCursor);

        
    }

    // Update is called once per frame
    void Update()
    {
        if (useCursor)
        {
            UpdateCursor();
        }

        if(Input.touchCount > 0 && Input.GetTouch(0).phase == TouchPhase.Began)
        {
            if (useCursor)
            {
                GameObject.Instantiate(objectToPlace, transform.position, transform.rotation);
            }
            else
            {
                List<ARRaycastHit> aRRaycastHits = new List<ARRaycastHit>();
                rayCastManager.Raycast(Input.GetTouch(0).position, aRRaycastHits, UnityEngine.XR.ARSubsystems.TrackableType.Planes);

                if (aRRaycastHits.Count > 0)
                {
                    GameObject.Instantiate(objectToPlace, aRRaycastHits[0].pose.position, aRRaycastHits[0].pose.rotation);
                }
            }
        }
    }

    void UpdateCursor()
    {
        Vector2 screenPossition = Camera.main.ViewportToScreenPoint(new Vector2(0.5f, 0.5f));
        List<ARRaycastHit> aRRaycastHits = new List<ARRaycastHit>();
        rayCastManager.Raycast(screenPossition, aRRaycastHits, UnityEngine.XR.ARSubsystems.TrackableType.Planes);

        if( aRRaycastHits.Count > 0)
        {
            transform.position = aRRaycastHits[0].pose.position;
            transform.rotation = aRRaycastHits[0].pose.rotation;
        }
    }
}
